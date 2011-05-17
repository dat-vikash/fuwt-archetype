package org.fuwt.examples;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.client.MockWebServiceServer;

import static net.javacrumbs.smock.common.client.CommonSmockClient.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


/**
 * User: chris
 * Date: 5/10/11
 * Time: 11:31 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/META-INF/spring/spring-core.xml",
                                   "classpath*:/META-INF/spring/spring-ws.xml"})
public class SampleWebServiceTest
{
    @Autowired
    SampleWebServiceGateway webServiceGateway;

    @Test
    public void shouldReturnStateAndCityForZip07302()
    {
        MockWebServiceServer mockServer=MockWebServiceServer.createServer(webServiceGateway);

        mockServer.expect(message("org/fuwt/examples/sample-web-service-expected-request.xml"))
                .andRespond(withMessage("org/fuwt/examples/sample-web-service-test-response.xml"));

        GetInfoByZipResponse response = webServiceGateway.getCityAndStateFromZip("07302");
        assertThat(response.getCity(), is(equalToIgnoringCase("Jersey City")));
        assertThat(response.getState(), is(equalToIgnoringCase("NJ")));

        mockServer.verify();
    }


}
