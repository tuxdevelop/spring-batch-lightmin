package org.tuxdevelop.spring.batch.lightmin.client.event;

public interface EventPublisher<T> {

    void publishEvent(final T eventData);
}
