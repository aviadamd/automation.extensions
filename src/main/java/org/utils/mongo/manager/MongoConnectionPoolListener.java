package org.utils.mongo.manager;

import com.mongodb.event.ConnectionCheckOutFailedEvent;
import com.mongodb.event.ConnectionCheckedOutEvent;
import com.mongodb.event.ConnectionPoolListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;

@Slf4j
public class MongoConnectionPoolListener implements ConnectionPoolListener {

    @Override
    public void connectionCheckedOut(final ConnectionCheckedOutEvent event) {
        try {
            log.info(String.format("Connection checkout with id %s...", event.getConnectionId().getLocalValue()));
        } catch (Exception exception) {
            Assertions.fail("connectionCheckedOut error: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void connectionCheckOutFailed(final ConnectionCheckOutFailedEvent event) {
        try {
            log.error("Something went wrong! Failed to checkout connection.");
        } catch (Exception exception) {
            Assertions.fail("connectionCheckOutFailed error: " + exception.getMessage(), exception);
        }
    }
}
