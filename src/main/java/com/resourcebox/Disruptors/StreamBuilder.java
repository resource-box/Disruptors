package com.resourcebox.Disruptors;

import java.util.List;

public class StreamBuilder<T> {
	
    private final List<Handler<T>> handlers;
    private final int bufferMultiplier;

    public StreamBuilder(List<Handler<T>> handlers, int bufferMultiplier) {
        this.handlers = handlers;
        this.bufferMultiplier = bufferMultiplier;
    }

    public Stream<T> build() {
        return new Stream<T>(handlers, bufferMultiplier);
    }
    
}
