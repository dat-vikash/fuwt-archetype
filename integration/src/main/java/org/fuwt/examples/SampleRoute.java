package org.fuwt.examples;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.milyn.Smooks;
import org.milyn.smooks.camel.processor.SmooksProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Sample of how to setup an Apache Camel Route.
 * <p/>
 * <p/>
 * User: chris
 * Date: 5/7/11
 * Time: 10:40 PM
 */
@Component
public class SampleRoute extends RouteBuilder
{

    private static final Logger logger = LoggerFactory.getLogger(SampleRoute.class);

    @Override
    public void configure() throws Exception
    {

        if (logger.isDebugEnabled()) getContext().setTracing(true);

        startHttpInterfaceRoute();
        startJPAPollingRoute();
        startActiveMQRoute();
    }


    private void startHttpInterfaceRoute()
    {
        //This route will read in an http request and route based on
        //the magic word paramter passed in.  If the route throws an exception
        //it will be handled by the onException block and return a 500 HTTP response
        from("jetty:http://localhost:9999/sampleroute")
                .routeId("sampleroute1")
                        //setup route sepcific exception handler
                .onException(Throwable.class)
                    .logStackTrace(true)
                    .maximumRedeliveries(4)
                    .to("log:org.fuwt.examples?level=INFO&showAll=true")
                    .transform(constant("Sorry couldn't process request: ").append(header("magicword")))
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                    .handled(true)
                .end()
                        //start main flow of the route which will do some content based routing
                        // according to header value
                .choice()
                    .when(header("magicword").isEqualTo("please"))
                        .transform(constant("<html><head/><body>No kitty! That's my pot pie.</pre></body></html>"))
                    .when(header("magicword").isEqualTo("fu"))
                        .log("log:org.fuwt.examples?level=DEBUG")
                        .throwException(new IllegalArgumentException("Not very nice"))
                    .when(header("magicword").isEqualTo("dumptodb"))
                        .transform(constant(new SampleModel("testing", false)))
                        .to("jpa:org.fuwt.examples.SampleModel")
                    .otherwise()
                        .transform(constant("<html><head/><body>Say the magic word...</body></html>"))
                .end();
    }


    private void startJPAPollingRoute() throws JAXBException, IOException, SAXException
    {
        //This route will continually poll the SampleModel DB table and consume
        // any samples marked as not syncd (i.e. "dirty").  It will then marshall that
        // POJO to xml and transform the xml to html using smooks.
        JAXBContext jaxbContext = JAXBContext.newInstance(SampleModel.class, XmlList.class);
        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat(jaxbContext);
        jaxbDataFormat.setPrettyPrint(true);
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/META-INF/smooks/examples/smooks-sample.xml"));
        from("jpa://org.fuwt.examples.SampleModel?persistenceUnit=main" +
             "&consumer.namedQuery=getAllDirtySamples" +
             "&consumeDelete=false" +
             "&consumeLockEntity=false" +
             "&consumer.delay=30000")
                .transacted()
                .routeId("sampleroute2")
                        //aggregate the consumed JPA records based on the time of the that batch the
                        //was consumed
                .aggregate(property("CamelCreatedTimestamp"), new GroupingAggregationStrategy())
                .completionFromBatchConsumer()
                .marshal(jaxbDataFormat)
                .to("log:org.fuwt.examples?level=INFO&showAll=true")
                .process(new SmooksProcessor(smooks, getContext()))
                .to("log:org.fuwt.examples?level=INFO")
                .to("activemq:queue:TEST?testConnectionOnStartup=true");
    }


    private void startActiveMQRoute()
    {
        //testing activemq connections
        from("activemq:queue:TEST?testConnectionOnStartup=true")
                .routeId("TEST")
                .log("Made it all the way here")
                .to("log:org.fuwt.examples?level=INFO");
    }
}
