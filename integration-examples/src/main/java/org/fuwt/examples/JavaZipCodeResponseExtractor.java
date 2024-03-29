package org.fuwt.examples;

import org.milyn.Smooks;
import org.milyn.payload.JavaResult;
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
@Component("javaInfoByZipResponseExtractor")
public class JavaZipCodeResponseExtractor implements SourceExtractor<GetInfoByZipResponse> {

    private static final Logger logger = LoggerFactory.getLogger(JavaZipCodeResponseExtractor.class);

    private final Smooks smooks;

    public JavaZipCodeResponseExtractor() {
        //need to initialize smooks transformation engine and ideally this is done only once
        //this instance will be specific for this transformation
        try {
            smooks = new Smooks(getClass().getResourceAsStream("/META-INF/smooks/examples/java-webservice-response-mapping.xml"));
        }
        catch (Throwable t) {
            throw new IllegalStateException("Unable to initialize smooks instance for " +
                                            "java-webservice-response-mapping.xml", t);
        }
    }

    public GetInfoByZipResponse extractData(final Source source) throws IOException, TransformerException {

        JavaResult javaResult = new JavaResult();
        smooks.filterSource(source, javaResult);

        if (logger.isTraceEnabled())
            logger.trace("Result of GetInfoByZipResponse transformation from XML to JAVA [" + javaResult + "]");

        //the bean id used to reyrieve the result is defined in the smooks mapping xml
        //that was initialized in the constructor
        return (GetInfoByZipResponse) javaResult.getBean("getInfoByZipResponse");
    }
}
