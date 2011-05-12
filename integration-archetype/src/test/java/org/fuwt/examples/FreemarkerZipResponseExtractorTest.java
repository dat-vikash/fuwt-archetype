package org.fuwt.examples;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.milyn.payload.StringSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.core.SourceExtractor;

import javax.annotation.Resource;
import javax.xml.transform.TransformerException;
import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
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
public class FreemarkerZipResponseExtractorTest
{
    @Resource(name = "xmlInfoByZipResponseExtractor")
    private SourceExtractor<String> extractor;

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
        String extract=extractor.extractData(new StringSource(testSoapResponseBody));
        assertThat(extract,containsString("Jersey City"));

    }
}
