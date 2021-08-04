package org.example.kafka.consumer.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerPoolFactory<K, V> extends BasePooledObjectFactory<PoolableConsumer<K, V>> {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerPool.class);
    private static AtomicInteger atomicInteger = new AtomicInteger();
    KafkaConsumerPool<K, V> objectPoolInstance;

    private final Properties consumerProperties = new Properties();

    private String groupPrefix = "kafka-consumer-pool";

    @SuppressWarnings("unchecked")
    @Override
    public PoolableConsumer<K, V> create() throws Exception {
        log.info("Dipesh Pool Factory Create consumer");
        //Properties props = new Properties(consumerProperties);
        String  groupdId = groupPrefix;
        if(consumerProperties.containsKey(ConsumerConfig.GROUP_ID_CONFIG))
            groupdId = consumerProperties.get(ConsumerConfig.GROUP_ID_CONFIG).toString();

        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupdId+"__"+ atomicInteger.getAndIncrement());
        PoolableConsumer<K, V> consumer =  (PoolableConsumer<K, V>) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { PoolableConsumer.class },
            new ConsumerProxy<K, V>(consumerProperties, objectPoolInstance));
        return consumer;
    }

    @Override
    public PooledObject<PoolableConsumer<K, V>> wrap(PoolableConsumer<K, V> obj) {
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public void destroyObject(final PooledObject<PoolableConsumer<K,V>> p)
        throws Exception  {
        log.info("Dipesh destroy object");
        p.getObject().destroyProxy();
    }


    @Override
    public boolean validateObject(final PooledObject<PoolableConsumer<K,V>> p) {
        log.info("Dipesh validateObject object");
        return p.getObject().validateProxy();
    }


    @Override
    public void activateObject(final PooledObject<PoolableConsumer<K,V>> p) throws Exception {
        log.info("Dipesh activateObject object");
        p.getObject().activateProxy();
    }


    @Override
    public void passivateObject(final PooledObject<PoolableConsumer<K,V>> p)
        throws Exception {
        log.info("Dipesh passivateObject object");
        p.getObject().passivateProxy();
    }

    public void setConsumerGroupPrefix(String prefix) {
        groupPrefix = prefix;
    }

    public Properties getConsumerProperties() {
        return consumerProperties;
    }
}
