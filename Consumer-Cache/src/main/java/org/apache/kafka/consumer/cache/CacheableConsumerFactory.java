package org.apache.kafka.consumer.cache;

import java.util.Properties;

public interface CacheableConsumerFactory<K, V>  {
    CacheableConsumer<K, V> create(Properties consumerProps) throws Exception;
    void destroyObject(final CacheableConsumer<K,V> consumer) throws Exception;
    boolean validateObject(final CacheableConsumer<K,V> consumer) throws Exception;
    void activateObject(final CacheableConsumer<K,V> consumer) throws Exception;
    void passivateObject(final CacheableConsumer<K,V> consumer) throws Exception;

}
