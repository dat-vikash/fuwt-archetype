package org.fuwt.examples;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: chris
 * Date: 5/12/11
 * Time: 1:49 AM
 */
public class GroupingAggregationStrategy implements AggregationStrategy {

    Logger logger = LoggerFactory.getLogger(GroupingAggregationStrategy.class);

    @SuppressWarnings("unchecked")
    public Exchange aggregate(final Exchange oldExchange, final Exchange newExchange) {

        final Exchange listExchange;
        final Object listItem=newExchange.getIn().getBody();
        if (oldExchange != null) {
            logger.debug("Adding to existing exchange grouping");
            traceExchange(oldExchange, "OLD");
            traceExchange(newExchange, "NEW");

            listExchange=oldExchange;
        }
        else {
            logger.debug("New exchange grouping is being statrted ");
            traceExchange(newExchange, "NEW");

            listExchange=newExchange;
            XmlList list = new XmlList();
            listExchange.getIn().setBody(list);
        }

        XmlList list = listExchange.getIn().getBody(XmlList.class);
        list.listItems().add(listItem);
        listExchange.getIn().setBody(list);

        return listExchange;
    }

    private void traceExchange(final Exchange exchange, final String exchangeName) {
        String traceMessage = new StringBuilder()
                .append(exchangeName)
                .append("[in={} out={}]").toString();
        logger.trace(traceMessage, exchange.getIn(), exchange.hasOut()?exchange.getOut():null);
    }
}
