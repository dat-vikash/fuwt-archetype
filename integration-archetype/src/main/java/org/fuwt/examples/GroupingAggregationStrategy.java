package org.fuwt.examples;

import org.apache.camel.Exchange;
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
        if (oldExchange != null) {
            logger.debug("Adding to existing exchange grouping");
            traceExchange(oldExchange, "OLD");
            traceExchange(newExchange, "NEW");


            XmlList list = (XmlList) oldExchange.getIn().getBody();
            list.listItems().add(newExchange.getIn().getBody());
            newExchange.getOut().setBody(list);
        }
        else {
            logger.debug("New exchange grouping is being statrted ");
            traceExchange(newExchange, "NEW");

            XmlList list = new XmlList();
            list.listItems().add(newExchange.getIn().getBody());
            newExchange.getOut().setBody(list);
        }


        return newExchange;
    }

    private void traceExchange(final Exchange exchange, final String exchangeName) {
        String traceMessage = new StringBuilder()
                .append(exchangeName)
                .append("[in={} out={}]").toString();
        logger.trace(traceMessage, exchange.getIn(), exchange.getOut());
    }
}
