package org.fuwt.examples;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * User: chris
 * Date: 5/12/11
 * Time: 10:55 PM
 */
public class SampleRouteTest extends IntegrationTestSupport
{

    private static final Logger logger= LoggerFactory.getLogger(SampleRouteTest.class);

    @Test
    public void httpRequestShouldReturnNoKitty() throws IOException
    {

        MockEndpoint mockEndpoint = getMockEndpoint("mock:http:localhost:9999/sampleroute");
//        mockEndpoint.whenAnyExchangeReceived(new Processor()
//        {
//            public void process(final Exchange exchange) throws Exception
//            {
//                System.out.println("FKDFJOHROGIHRO988)(*)(**)(*)(*)(");
//                throw new IllegalAccessException();
//            }
//        });


        Object s=template.requestBodyAndHeader("direct:httpcall","","magicword","please");

        logger.info(String.valueOf(s));
    }


    @Override
    protected RouteBuilder createRouteBuilder() throws Exception
    {
        return new SampleRouteIntegrationHooks();
    }

    @Override
    protected AbstractApplicationContext createApplicationContext()
    {
        return new ClassPathXmlApplicationContext(
                "/spring/test/spring-core-test.xml",
                "/META-INF/spring/spring-datasource.xml",
                "/spring/test/spring-camel-mock.xml");
    }

    private class SampleRouteIntegrationHooks extends SampleRoute
    {
        @Override
        public void configure() throws Exception
        {
            super.configure();
            from("direct:httpcall")
            .to("jetty:http://localhost:9999/sampleroute");

        }
    }
}
