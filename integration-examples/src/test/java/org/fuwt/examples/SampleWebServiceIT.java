package org.fuwt.examples;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: chris
 * Date: 5/10/11
 * Time: 11:31 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/META-INF/spring/spring-core.xml",
                                   "classpath*:/META-INF/spring/spring-ws.xml"})
public class SampleWebServiceIT
{
    @Autowired
    SampleWebServiceGateway webServiceGateway;

    @Test
    @Ignore("Takes too long to run reliably during build")
    public void shouldReturnStateAndCityForZip07302()
    {
        GetInfoByZipResponse response = webServiceGateway.getCityAndStateFromZip("07302");
        assertThat(response.getCity(), is(equalToIgnoringCase("Jersey City")));
        assertThat(response.getState(), is(equalToIgnoringCase("NJ")));
    }


}
