package com.resourcebox.Disruptors;

public class StreamManager<T> {

    private final Stream<T>[] streamList;
    private final boolean isSingleStream;
    private final ThreadLocal<Integer> threadLocalIndex;

    public StreamManager(Stream<T>[] streamList) {
        this.streamList = streamList;
        this.isSingleStream = (streamList != null && streamList.length == 1);
        this.threadLocalIndex = this.isSingleStream ? null : ThreadLocal.withInitial(() -> 0);
    }

    public Stream<T> getNextStream() {
        if (streamList == null || streamList.length == 0) {
            return null;
        }

        if (isSingleStream) {
            return streamList[0];
        }
        
        int startIndex = threadLocalIndex.get();
        int currentIndex = startIndex;

        do {
            Stream<T> stream = streamList[currentIndex];
            if (stream.hasAvailableCapacity(1)) {
                threadLocalIndex.set((currentIndex + 1) % streamList.length);
                return stream;
            }
            currentIndex = (currentIndex + 1) % streamList.length;
        } while (currentIndex != startIndex);
        

        Stream<T> fallbackStream = streamList[startIndex];
        threadLocalIndex.set((startIndex + 1) % streamList.length);
        
        return fallbackStream;
    }

}
