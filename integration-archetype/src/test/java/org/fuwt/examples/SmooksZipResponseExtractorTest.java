package org.fuwt.examples;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.milyn.payload.StringSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.xml.transform.TransformerException;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: chris
 * Date: 5/11/11
 * Time: 1:53 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/spring-core.xml")
public class SmooksZipResponseExtractorTest
{
    @Resource(name = "infoByZipResponseExtractor")
    private SmooksZipResponseExtractor extractor;

    @Test
    public void shouldConvertResponseFromXMLToJava() throws TransformerException, IOException
    {
        final String testSoapResponseBody="<GetInfoByZIPResponse xmlns=\"http://www.webserviceX.NET\">" +
                                          "<GetInfoByZIPResult>" +
                                          "<NewDataSet xmlns=\"\">" +
                                          "<Table><CITY>Jersey City</CITY><STATE>NJ</STATE>" +
                                          "<ZIP>07302</ZIP><AREA_CODE>201</AREA_CODE><TIME_ZONE>E</TIME_ZONE>" +
                                          "</Table></NewDataSet>" +
                                          "</GetInfoByZIPResult>" +
                                          "</GetInfoByZIPResponse>";
        GetInfoByZipResponse response=extractor.extractData(new StringSource(testSoapResponseBody));
        assertThat(response.getCity(), is(equalTo("Jersey City")));
        assertThat(response.getState(), is(equalTo("NJ")));

    }
}
