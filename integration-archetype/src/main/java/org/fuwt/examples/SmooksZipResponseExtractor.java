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
@Component("infoByZipResponseExtractor")
public class SmooksZipResponseExtractor implements SourceExtractor<GetInfoByZipResponse>
{

    private static final Logger logger= LoggerFactory.getLogger(SmooksZipResponseExtractor.class);

    private final Smooks smooks;

    public SmooksZipResponseExtractor()
    {
        try
        {
            smooks=new Smooks(getClass().getResourceAsStream("/META-INF/smooks/smooks-webservice-response-mapping.xml"));
        }
        catch (Throwable t)
        {
            throw new IllegalStateException("Unable to initialize smooks instance for " +
                                            "smooks-webservice-response-mapping.xml",t);
        }
    }

    public GetInfoByZipResponse extractData(final Source source) throws IOException, TransformerException
    {

        JavaResult javaResult=new JavaResult();
        smooks.filterSource(source,javaResult);

        if(logger.isTraceEnabled())
            logger.trace("Result of GetInfoByZipResponse transformation from XML to JAVA [" + javaResult + "]");

        return (GetInfoByZipResponse) javaResult.getBean("getInfoByZipResponse");
    }
}
