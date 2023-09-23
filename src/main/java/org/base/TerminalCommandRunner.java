package org.base;

import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class TerminalCommandRunner {

    public static void runCommand(boolean isWindows, String command)  {
        try {

            ProcessBuilder builder = new ProcessBuilder();

            if (isWindows) {
                builder.command("cmd.exe", "/c", command);
            } else builder.command("sh", "-c", command);

            builder.directory(new File(System.getProperty("user.home")));
            Process process = builder.start();

            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), log::debug);
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            Future<?> future = executorService.submit(streamGobbler);

            int exitCode = process.waitFor();

            assertDoesNotThrow(() -> future.get(10, TimeUnit.SECONDS));
            assertEquals(0, exitCode);

            executorService.shutdown();
        } catch (IOException | InterruptedException ignored) {}
    }
}
