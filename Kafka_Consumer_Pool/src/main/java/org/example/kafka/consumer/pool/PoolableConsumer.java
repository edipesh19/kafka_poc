package org.example.kafka.consumer.pool;

import org.apache.kafka.clients.consumer.Consumer;

public interface PoolableConsumer<K, V> extends Consumer<K, V> {
    void destroyProxy();
    boolean validateProxy();
    void activateProxy();
    void passivateProxy();
}


