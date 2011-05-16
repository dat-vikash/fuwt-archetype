package org.fuwt.examples;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.hsqldb.jdbc.jdbcDataSource;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import javax.jms.ConnectionFactory;

/**
 * Supports running local integration tests by bootstrapping the encessary JNDI
 * components needed to support the integration framework
 *
 * User: chris
 * Date: 5/13/11
 * Time: 1:08 AM
 */
public abstract class IntegrationTestSupport extends CamelSpringTestSupport
{

    Logger logger = LoggerFactory.getLogger(IntegrationTestSupport.class);


    @Before
    public void setUp() throws Exception
    {
        setupJNDIContext();
        super.setUp();
    }

    public void setupJNDIContext()
    {
        try
        {
            logger.info("starting jndi...");
            final SimpleNamingContextBuilder jndiBuilder = new SimpleNamingContextBuilder();
            jdbcDataSource dataSource = new jdbcDataSource();
            dataSource.setUser("sa");
            dataSource.setPassword("");
            dataSource.setDatabase("jdbc:hsqldb:mem:testdb");
            jndiBuilder.bind("jdbc/testDS", dataSource);

            ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
            jndiBuilder.bind("jms/connectionFactory",connectionFactory);

            jndiBuilder.activate();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
