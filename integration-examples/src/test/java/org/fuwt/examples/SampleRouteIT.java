package org.fuwt.examples;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.regex.Pattern;

import static org.fuwt.examples.RegExMatcher.matches;

/**
 * User: chris
 * Date: 5/12/11
 * Time: 10:55 PM
 */
public class SampleRouteIT extends IntegrationTestSupport {


    @Test
    public void httpRequestShouldReturnNoKitty() throws IOException, InterruptedException {

        //setup test expectation for receiving the intial message
        MockEndpoint startofroute = getMockEndpoint("mock:http:localhost:9999/sampleroute");
        startofroute.setResultWaitTime(1000);
        startofroute.expectedMessageCount(1);


        //setup test expectation for the end result of the entire message flow
        MockEndpoint endofqueue = getMockEndpoint("mock:endofsaleforcequeue");
        endofqueue.setResultWaitTime(1000);
        endofqueue.expectedMessageCount(1);


        String response = template.requestBodyAndHeader("http://localhost:9999/sampleroute",
                                                        "",
                                                        "magicword",
                                                        "dumptodb",
                                                        String.class);

        assertThat(response, matches("<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>\n" +
                                     "<sample id=\".*?\">\n" +
                                     "    <name>testing</name>\n" +
                                     "    <syncd>false</syncd>\n" +
                                     "</sample>", Pattern.DOTALL));

        startofroute.assertIsSatisfied();
        endofqueue.assertIsSatisfied();

        if (!endofqueue.getExchanges().isEmpty()) {
            Exchange exchange = endofqueue.getExchanges().get(0);
            assertThat(exchange.getIn().getBody(String.class), matches("<list>\n" +
                                                                       "    <listitem id=\".*?\">\n" +
                                                                       "        <name>testing</name>\n" +
                                                                       "        <syncd>false</syncd>\n" +
                                                                       "    </listitem>\n" +
                                                                       "</list>", Pattern.DOTALL));
        }

    }


    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new SampleRoute();
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(
                "/spring/test/spring-core-test.xml",
                "/META-INF/spring/spring-datasource.xml",
                "/spring/test/spring-camel-test.xml");
    }

}
