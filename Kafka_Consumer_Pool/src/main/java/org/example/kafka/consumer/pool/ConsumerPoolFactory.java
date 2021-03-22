package org.example.kafka.consumer.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerPoolFactory<K, V> extends BasePooledObjectFactory<PoolableConsumer<K, V>> {
    private static AtomicInteger n = new AtomicInteger();
    KafkaConsumerPool<K, V> objectPoolInstance;

    private final Properties consumerProperties = new Properties();

    @SuppressWarnings("unchecked")
    @Override
    public PoolableConsumer<K, V> create() throws Exception {
        //Properties props = new Properties(consumerProperties);
        String  groupdId = groupPrefix;
        if(consumerProperties.containsKey(ConsumerConfig.GROUP_ID_CONFIG))
            groupdId = consumerProperties.get(ConsumerConfig.GROUP_ID_CONFIG).toString();

        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupdId+"__"+n.getAndIncrement());
        PoolableConsumer<K, V> consumer =  (PoolableConsumer<K, V>) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { PoolableConsumer.class },
            new ConsumerProxy<K, V>(consumerProperties, objectPoolInstance));
        return consumer;
    }

    @Override
    public PooledObject<PoolableConsumer<K, V>> wrap(PoolableConsumer<K, V> obj) {
        return new DefaultPooledObject<>(obj);
    }

    public Properties getConsumerProperties() {
        return consumerProperties;
    }

    @Override
    public void destroyObject(final PooledObject<PoolableConsumer<K,V>> p)
        throws Exception  {
        p.getObject().destroyProxy();
    }


    @Override
    public boolean validateObject(final PooledObject<PoolableConsumer<K,V>> p) {
        return p.getObject().validateProxy();
    }


    @Override
    public void activateObject(final PooledObject<PoolableConsumer<K,V>> p) throws Exception {
        p.getObject().activateProxy();
    }


    @Override
    public void passivateObject(final PooledObject<PoolableConsumer<K,V>> p)
        throws Exception {
        p.getObject().passivateProxy();
    }

    private String groupPrefix = "kafka-consumer-pool";
    public void setConsumerGroupPrefix(String prefix) {
        groupPrefix = prefix;
    }
}
