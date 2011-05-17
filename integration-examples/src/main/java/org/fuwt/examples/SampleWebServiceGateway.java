package org.fuwt.examples;

import org.springframework.ws.client.core.SourceExtractor;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;

/**
 * User: chris
 * Date: 5/10/11
 * Time: 7:29 PM
 */
public class SampleWebServiceGateway extends WebServiceGatewaySupport {

    @Resource(name = "javaInfoByZipResponseExtractor")
    private SourceExtractor<GetInfoByZipResponse> infoByZipResponseExtractor;


    public void setInfoByZipResponseExtractor(SourceExtractor<GetInfoByZipResponse> infoByZipResponseExtractor) {
        this.infoByZipResponseExtractor = infoByZipResponseExtractor;
    }

    /**
     * I stopped calling this method because the test web service I choose happens
     * not to implement the web service spec correctly.  They are not handlined the xml namespace
     * spec so when you include a prefix for the namespace it is failing.  Unfortunately JAXB
     * always include namespaces in this way.  This is an example of why sometimes strict
     * framework binding can back you into a corner, and is a case for sticking to Spring's
     * lower level WebServiceTemplate to handle these cases.
     * <p/>
     * Feel free to change this class to marshall this request to watch it fail.
     *
     * @param request - the request that will be turned into a SOAP message
     * @return marshalled Source for the GetInfoByZipRequest
     */
    private Source marshall(GetInfoByZipRequest request) {

        StringResult result = new StringResult();

        try {
            getMarshaller().marshal(request, result);
            return new StreamSource(new StringReader(result.toString()));
        }
        catch (IOException e) {
            throw new IllegalStateException("Unable to marshall GetInfoByZipRequest: " + request.toString());
        }

    }


    public GetInfoByZipResponse getCityAndStateFromZip(final String zipcode) {

        SoapActionCallback soapActionCallback = new SoapActionCallback("http://www.webserviceX.NET/GetInfoByZIP");
        //Since this sample web service provider can't handle namespace prefixes can't use JAXB here.
        String request = new StringBuilder().append("<GetInfoByZIP xmlns=\"http://www.webserviceX.NET\">")
                .append("<USZip>").append(zipcode).append("</USZip></GetInfoByZIP>").toString();


        return getWebServiceTemplate().sendSourceAndReceive(new StringSource(request),
                                                            soapActionCallback,
                                                            infoByZipResponseExtractor);

    }

}
