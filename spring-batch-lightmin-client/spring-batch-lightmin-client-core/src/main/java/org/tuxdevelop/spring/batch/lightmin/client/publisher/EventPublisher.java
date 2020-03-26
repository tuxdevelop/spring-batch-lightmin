package org.tuxdevelop.spring.batch.lightmin.client.publisher;

public interface EventPublisher<T> {

    void publishEvent(final T eventData);
}
