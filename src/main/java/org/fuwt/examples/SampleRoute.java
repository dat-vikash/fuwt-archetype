package org.fuwt.examples;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.milyn.Smooks;
import org.milyn.smooks.camel.processor.SmooksProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

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

    @XmlRootElement(name = "list")
    @XmlAccessorType(value = XmlAccessType.FIELD)
    public static class XmlList
    {
        @XmlElements(value = {@XmlElement(name = "sample", type = SampleModel.class)})
        private List listItems = new ArrayList();

        public List listItems()
        {
            return listItems;
        }

    }

    private static class GroupingAggregationStrategy implements AggregationStrategy
    {
        @SuppressWarnings("unchecked")
        public Exchange aggregate(final Exchange oldExchange, final Exchange newExchange)
        {
            if (oldExchange != null)
            {
                logger.debug("Adding to existing exchange grouping");
                traceExchange(oldExchange, "OLD");
                traceExchange(newExchange, "NEW");


                XmlList list = (XmlList) oldExchange.getIn().getBody();
                list.listItems().add(newExchange.getIn().getBody());
                newExchange.getOut().setBody(list);
            }
            else
            {
                logger.trace("New exchange grouping is being statrted ");
                traceExchange(newExchange, "NEW");

                XmlList list = new XmlList();
                list.listItems().add(newExchange.getIn().getBody());
                newExchange.getOut().setBody(list);
            }


            return newExchange;
        }

        private void traceExchange(final Exchange exchange, final String exchangeName)
        {
            String traceMessage = new StringBuilder()
                    .append(exchangeName)
                    .append("[in={} out={}]").toString();
            logger.trace(traceMessage, exchange.getIn(), exchange.getOut());
        }
    }


    @Override
    public void configure() throws Exception
    {


        if (logger.isDebugEnabled()) getContext().setTracing(true);

        //This route will read in an http request and route based on
        //the magic word paramter passed in.  If the route throws an exception
        //it will be handled by the onException block and return a 500 HTTP response
        from("jetty:http://localhost:9999/sampleroute")
                .routeId("sampleroute1")
                        //setup rpute sepcific exception handler
                .onException(Throwable.class)
                .logStackTrace(true)
                .maximumRedeliveries(4)
                .to("log:?level=INFO&showAll=true")
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
                .log("log:${groupId}.examples?level=DEBUG")
                .process(new Processor()
                {
                    public void process(final Exchange exchange) throws Exception
                    {
                        System.out.println("in here");
                        throw new IllegalArgumentException("Not very nice");
                    }
                })
                .when(header("magicword").isEqualTo("dumptodb"))
                .transform(constant(new SampleModel("testing", false)))
                .to("jpa:${groupId}.examples.SampleModel")
                .otherwise()
                .transform(constant("<html><head/><body>Say the magic word...</body></html>"))
                .end();


        //This route will continually poll the SampleModel DB table and consume
        // any samples marked as not syncd (i.e. "dirty").  It will then marshall that
        // POJO to xml and transform the xml to html using smooks.
        JAXBContext jaxbContext = JAXBContext.newInstance(SampleModel.class, XmlList.class);
        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat(jaxbContext);
        jaxbDataFormat.setPrettyPrint(true);
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/META-INF/smooks/smooks-sample.xml"));
        from("jpa://examples.SampleModel?persistenceUnit=main" +
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
                .to("log:${groupId}.examples?level=INFO&showAll=true")
                .process(new SmooksProcessor(smooks, getContext()))
                .to("log:${groupId}.examples?level=INFO")
                .to("activemq:queue:TEST?testConnectionOnStartup=true");


        //testing activemq connections
        from("activemq:queue:TEST?testConnectionOnStartup=true")
                .log("Made it all the way here")
                .to("log:${groupId}.examples?level=INFO");


    }
}
