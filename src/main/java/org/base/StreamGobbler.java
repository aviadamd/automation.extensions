package org.base;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

class StreamGobbler implements Runnable {
    private final InputStream inputStream;
    private final Consumer<String> consumer;

    public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        InputStreamReader inputStreamReader = new InputStreamReader(this.inputStream);
        new BufferedReader(inputStreamReader)
                .lines()
                .forEach(this.consumer);
    }
}
