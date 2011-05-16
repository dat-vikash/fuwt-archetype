package org.fuwt.examples;

import org.milyn.Smooks;
import org.milyn.payload.StringResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.SourceExtractor;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * User: chris
 * Date: 5/10/11
 * Time: 9:17 PM
 */
@Component("xmlInfoByZipResponseExtractor")
public class XmlZipResponseExtractor implements SourceExtractor<String> {

    private static final Logger logger = LoggerFactory.getLogger(XmlZipResponseExtractor.class);

    private final Smooks smooks;

    public XmlZipResponseExtractor() {
        try {
            smooks = new Smooks(getClass().getResourceAsStream("/META-INF/smooks/examples/java-webservice-response-mapping.xml"));
        }
        catch (Throwable t) {
            throw new IllegalStateException("Unable to initialize smooks instance for " +
                                            "java-webservice-response-mapping.xml", t);
        }
    }

    public String extractData(final Source source) throws IOException, TransformerException {

        StringResult stringResult = new StringResult();
        smooks.filterSource(source, stringResult);

        if (logger.isTraceEnabled())
            logger.trace("Result of GetInfoByZipResponse transformation from XML to XML [ {} ] ", stringResult);

        return stringResult.getResult();
    }
}
