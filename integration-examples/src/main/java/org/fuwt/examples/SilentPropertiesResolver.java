package org.fuwt.examples;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.DefaultPropertiesResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * User: chris
 * Date: 5/22/11
 * Time: 2:23 PM
 */
public class SilentPropertiesResolver extends DefaultPropertiesResolver {

    private static final Logger logger = LoggerFactory.getLogger(SilentPropertiesResolver.class);

    @Override
    public Properties resolveProperties(final CamelContext context, final String... uri) throws Exception {

        final Properties properties = new Properties();
        for (String path : uri) {
            try {
                properties.putAll(super.resolveProperties(context, path));
            }
            catch (Throwable t) {
                logger.trace("Apache Camel was unable to load properties file from location: " + uri, t);
            }

        }

        return properties;
    }
}
