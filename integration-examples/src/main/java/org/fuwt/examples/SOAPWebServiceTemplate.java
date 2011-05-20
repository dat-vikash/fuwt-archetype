package org.fuwt.examples;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.SourceExtractor;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import java.io.IOException;

/**
 * User: cbarrese
 * Date: 5/19/11
 * Time: 11:26 AM
 * <p/>
 * Extension of Spring WebServiceTemplate to support nicer method signatures for dealing with
 * SOAP header and SOAP action
 */
public class SOAPWebServiceTemplate extends WebServiceTemplate {

    public <T> T sendSourceAndReceive(final Source requestPayload,
                                      final Source requestHeader,
                                      final SourceExtractor<T> responseExtractor) {
        return super.sendSourceAndReceive(requestPayload,
                new WebServiceMessageCallback() {

                    public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {

                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        transformer.transform(requestHeader, ((SoapMessage) message).getSoapHeader().getResult());
                    }
                }, responseExtractor);
    }

    public <T> T sendSourceAndReceive(final Source requestPayload,
                                      final Source requestHeader,
                                      final String action,
                                      final SourceExtractor<T> responseExtractor) {
        return super.sendSourceAndReceive(requestPayload,
                new WebServiceMessageCallback() {

                    public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {

                        SoapMessage soapMessage=((SoapMessage) message);
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        transformer.transform(requestHeader, soapMessage.getSoapHeader().getResult());

                        soapMessage.setSoapAction(action);

                    }
                }, responseExtractor);
    }

    public <T> T sendSourceAndReceive(final Source requestPayload,
                                      final String action,
                                      final SourceExtractor<T> responseExtractor) {
        return super.sendSourceAndReceive(requestPayload,new SoapActionCallback(action), responseExtractor);
    }

}
