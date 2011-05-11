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
 * Sample of how to setup an Apache Camel Route
 * <p/>
 * User: chris
 * Date: 5/7/11
 * Time: 10:40 PM
 */
@Component
public class SampleRoute extends RouteBuilder
{

    Logger logger= LoggerFactory.getLogger(SampleRoute.class);

    @XmlRootElement(name = "samples")
    @XmlAccessorType(value = XmlAccessType.FIELD)
    public static class SampleModelList
    {
        @XmlElements(value = {@XmlElement(name = "sample" ,type = SampleModel.class)})
        private List<SampleModel> samples=new ArrayList<SampleModel>();

        public List<SampleModel> getSamples()
        {
            return samples;
        }

        public void setSamples(final List<SampleModel> samples)
        {
            this.samples = samples;
        }
    }


    @Override
    public void configure() throws Exception
    {


        if(logger.isTraceEnabled())getContext().setTracing(true);

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
                .transform(constant("<html><head/><body>No kitty! That's my pot pie." +
                                    "                         <pre>\n" +
                                    "                \\`*-.                    \n" +
                                    "                 )  _`-.                 \n" +
                                    "                .  : `. .                \n" +
                                    "                : _   '  \\               \n" +
                                    "                ; *` _.   `*-._          \n" +
                                    "                `-.-'          `-.       \n" +
                                    "                  ;       `       `.     \n" +
                                    "                  :.       .        \\    \n" +
                                    "                  . \\  .   :   .-'   .   \n" +
                                    "                  '  `+.;  ;  '      :   \n" +
                                    "                  :  '  |    ;       ;-. \n" +
                                    "                  ; '   : :`-:     _.`* ;\n" +
                                    "               .*' /  .*' ; .*`- +'  `*' \n" +
                                    "               `*-*   `*-*  `*-*' </pre></body></html>"))
            .when(header("magicword").isEqualTo("fu"))
                .log("log:org.fuwt?level=DEBUG")
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
                .to("jpa:org.fuwt.SampleModel")
            .otherwise()
                .transform(constant("<html><head/><body>Say the magic word...</body></html>"))
                .end();


        //This route will continually poll the SampleModel DB table and consume
        // any samples marked as not syncd (i.e. "dirty").  It will then marshall that
        // POJO to xml and transform the xml to html using smooks.
        JAXBContext jaxbContext=JAXBContext.newInstance(SampleModel.class,SampleModelList.class);
        JaxbDataFormat jaxbDataFormat=new JaxbDataFormat(jaxbContext);
        jaxbDataFormat.setPrettyPrint(true);
        Smooks smooks=new Smooks(getClass().getResourceAsStream("/META-INF/smooks/smooks-sample.xml"));
        from("jpa://SampleModel?persistenceUnit=main" +
             "&consumer.namedQuery=getAllDirtySamples" +
             "&consumeDelete=false" +
             "&consumeLockEntity=false" +
             "&consumer.delay=30000")
                .routeId("sampleroute2")
                .aggregate(property("CamelCreatedTimestamp"), new AggregationStrategy()
                {
                    public Exchange aggregate(final Exchange oldExchange, final Exchange newExchange)
                    {
                        if(oldExchange!=null)
                        {
                            System.out.println(oldExchange.getIn() +" " +oldExchange.getOut());
                            System.out.println(newExchange.getIn() +" " +newExchange.getOut());
                            SampleModelList list= (SampleModelList) oldExchange.getIn().getBody();
                            list.getSamples().add((SampleModel) newExchange.getIn().getBody());
                            newExchange.getOut().setBody(list);
                        }
                        else
                        {
                            System.out.println("NULL");
                            System.out.println(newExchange.getIn() +" " +newExchange.getOut());
                            SampleModelList list=new SampleModelList();
                            list.getSamples().add((SampleModel) newExchange.getIn().getBody());
                            newExchange.getOut().setBody(list);
                        }


                        return newExchange;
                    }
                })
                .completionFromBatchConsumer()
                .log("Grouped")
                .marshal(jaxbDataFormat)
                .to("log:org?level=INFO&showAll=true")
                .to("log:com?level=INFO&showAll=true")
                .process(new SmooksProcessor(smooks, getContext()))
                .to("log:org.fuwt?level=INFO")
                .to("activemq:queue:TEST?testConnectionOnStartup=true");


        from("activemq:queue:TEST?testConnectionOnStartup=true")
        .log("Made it all the way here")
        .to("log:org.fuwt?level=INFO");


    }
}
