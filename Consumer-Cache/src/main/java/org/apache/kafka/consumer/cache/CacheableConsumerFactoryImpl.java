package org.apache.kafka.consumer.cache;

import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.Properties;

@Component
public class CacheableConsumerFactoryImpl<K, V> implements CacheableConsumerFactory<K, V> {

    @Override
    public CacheableConsumer<K, V> create(Properties consumerProps) throws Exception {

        CacheableConsumer<K, V> consumer =  (CacheableConsumer<K, V>) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { CacheableConsumer.class },
            new ConsumerProxy<K, V>(consumerProps));
        return consumer;
    }

    @Override
    public void destroyObject(final CacheableConsumer<K,V> consumer)
        throws Exception  {
        consumer.destroyProxy();
    }


    @Override
    public boolean validateObject(final CacheableConsumer<K,V> consumer) throws Exception {
        return consumer.validateProxy();
    }


    @Override
    public void activateObject(final CacheableConsumer<K,V> consumer) throws Exception {
        consumer.activateProxy();
    }


    @Override
    public void passivateObject(final CacheableConsumer<K,V> consumer)
        throws Exception {
        consumer.passivateProxy();
    }
}
