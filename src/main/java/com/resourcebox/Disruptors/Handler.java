package com.resourcebox.Disruptors;

import com.lmax.disruptor.EventHandler;
import lombok.Getter;

@Getter
public class Handler<T> implements EventHandler<Event<T>> {

    @Override
    public void onEvent(Event<T> event, long sequence, boolean endOfBatch) {
        process(event);    
    }

    protected void process(Event<T> event) { }

}
