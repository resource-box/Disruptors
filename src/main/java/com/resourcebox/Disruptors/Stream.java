package com.resourcebox.Disruptors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import java.util.List;

public class Stream<T> {

    private final Disruptor<Event<T>> disruptor;
    private final RingBuffer<Event<T>> ringBuffer;

    public Stream(List<Handler<T>> handlers, int bufferMultiplier) {
        disruptor = new Disruptor<>(
                Event::new,
                1024 * bufferMultiplier,
                DaemonThreadFactory.INSTANCE,
                ProducerType.MULTI,
                new SleepingWaitStrategy()
        );

        if (handlers.size() > 1) {
            disruptor.handleEventsWith(handlers.get(0));
            for (int i = 1; i < handlers.size(); i++) {
            	if (i != handlers.size() - 1) {
            		disruptor.after(handlers.get(i - 1)).handleEventsWith(handlers.get(i));
            	} else {
            		disruptor.after(handlers.get(i - 1)).handleEventsWith(handlers.get(i))
            			.then(new ClearingEventHandler<T>());
            	}
            }
        } else {
            disruptor.handleEventsWith(handlers.get(0));
        }

        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    public boolean hasAvailableCapacity(int requiredCapacity) {
        return ringBuffer.hasAvailableCapacity(requiredCapacity);
    }

    public void publishInitialEvent(T initialData) {
        long sequence = ringBuffer.next();
        try {
            Event<T> event = ringBuffer.get(sequence);
            event.setData(initialData);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    public void stop() throws Exception {
        disruptor.shutdown();
    }

}
