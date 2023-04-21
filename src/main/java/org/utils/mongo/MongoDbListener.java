package org.utils.mongo;

import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandSucceededEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MongoDbListener implements CommandListener {
    private final Map<String, Integer> commands = new HashMap<>();

    @Override
    public synchronized void commandSucceeded(final CommandSucceededEvent event) {
        try {
            String commandName = event.getCommandName();
            int count =  this.commands.getOrDefault(commandName, 0);
            this.commands.put(commandName, count + 1);
            log.info("command succeeded " +  this.commands + " with response " + event.getResponse().toJson());
        } catch (Exception exception) {
            Assertions.fail("commandSucceeded error: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void commandFailed(final CommandFailedEvent event) {
        try {
            String commandName = event.getCommandName();
            int requestId = event.getRequestId();
            Assertions.fail(String.format("Failed execution of command '%s' with id %s", commandName, requestId));
        } catch (Exception exception) {
            Assertions.fail("commandFailed error: " + exception.getMessage(), exception);
        }
    }
}
