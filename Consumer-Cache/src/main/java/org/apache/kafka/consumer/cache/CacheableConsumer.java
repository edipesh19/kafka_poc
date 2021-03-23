package org.apache.kafka.consumer.cache;

import org.apache.kafka.clients.consumer.Consumer;

public interface CacheableConsumer<K, V> extends Consumer<K,V> {
    void destroyProxy();
    boolean validateProxy();
    void activateProxy();
    void passivateProxy();
}
