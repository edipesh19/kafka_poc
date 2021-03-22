package org.example.kafka.consumer.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaConsumerPool<K, V> extends GenericObjectPool<PoolableConsumer<K, V>> {

    public static final String PROP_POOL_HEARTBEAT = "pool.heartbeatThreadEnabled";
    public static final String PROP_POOL_MAX_SIZE = "pool.maxTotal";
    public static final String PROP_POOL_TEST = "pool.testOnBorrow";
    public static final String PROP_POOL_MAXWAIT = "pool.maxWaitMillis";
    public static final String PROP_POOL_EVICT_PERIOD = "pool.timeBetweenEvictionRunsMillis";

    private boolean heartbeatEnabled;
    boolean isHeartbeatThreadEnabled(){
        return heartbeatEnabled;
    }
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerPool.class);

    @Autowired
    private KafkaClientsConfig kafkaProperties;

    public KafkaConsumerPool() {
        this(new ConsumerPoolFactory<K, V>());
        log.info("========================= KafkaConsumerPool ========================= ");
    }

    private Properties consumerProperties;

    @PostConstruct
    void init(){
        consumerProperties = kafkaProperties.getConsumerConfig();

        //spring.kafka.consumer.properties.pool.*
        Properties custom = (Properties) consumerProperties.get("properties");
        if(custom == null)
            custom = new Properties();

        setConsumerProperties(consumerProperties);

        setMaxTotal(Integer.parseInt( custom.getProperty(PROP_POOL_MAX_SIZE, "10")) );
        setTestOnBorrow(Boolean.parseBoolean(custom.getProperty(PROP_POOL_TEST, "true")));
        setMaxWaitMillis(Long.parseLong( custom.getProperty(PROP_POOL_MAXWAIT, "5000")));
        setTimeBetweenEvictionRunsMillis(Long.parseLong( custom.getProperty(PROP_POOL_EVICT_PERIOD, "5000")));

        setPoolProperties(custom, "pool.");
        poolFactory.objectPoolInstance = this;
        heartbeatEnabled = Boolean.parseBoolean( custom.getProperty(PROP_POOL_HEARTBEAT, "false"));

        log.info("Initialized consumer pool of- size -> {}, maxidle {}, waitms {}", getMaxTotal(), getMaxIdle(), getMaxWaitMillis());
        log.info("========================= KafkaConsumerPool init ========================= ");
    }

    /**
     * Acquire consumer starting from a given offset
     * @param maxwait
     * @param unit
     * @param topicPartitionOffset
     * @return
     * @throws Exception
     */
    public Consumer<K, V> acquire(long maxwait, TimeUnit unit, Map<TopicPartition, Long> topicPartitionOffset) throws Exception {
        PoolableConsumer<K, V> consumer = borrowObject(unit.toMillis(maxwait));
        consumer.assign(topicPartitionOffset.keySet());
        topicPartitionOffset.keySet().forEach(tp -> {
            consumer.seek(tp, topicPartitionOffset.get(tp));
        });
        return consumer;
    }

    /**
     * Acquire consumer starting from beginning offset
     * @param maxwait
     * @param unit
     * @param topicPartitions
     * @return
     * @throws Exception
     */
    public Consumer<K, V> acquireLatest(long maxwait, TimeUnit unit, TopicPartition... topicPartitions) throws Exception {
        PoolableConsumer<K, V> consumer = borrowObject(unit.toMillis(maxwait));
        List<TopicPartition> partitionList = Arrays.asList(topicPartitions);
        consumer.assign(partitionList);
        consumer.seekToEnd(partitionList);
        return consumer;
    }
    public void release(Consumer<K, V> pooled) {
        if(!(pooled instanceof PoolableConsumer))
            throw new IllegalArgumentException("Not a type of PoolableConsumer!");
        returnObject((PoolableConsumer<K, V>) pooled);
    }
    private final ConsumerPoolFactory<K, V> poolFactory;
    private KafkaConsumerPool(ConsumerPoolFactory<K, V> thePool) {
        super(thePool);
        this.poolFactory = thePool;
    }
    public void setConsumerGroupPrefix(String prefix) {
        poolFactory.setConsumerGroupPrefix(prefix);
    }
    public void setConsumerProperty(String prop, Object val) {
        poolFactory.getConsumerProperties().put(prop, val);
    }

    public void setConsumerProperties(Properties props) {
        poolFactory.getConsumerProperties().putAll(props);
    }

    void setPoolProperties(Properties props, String prefix) {
        copyBeanProperties(this, props, prefix);
    }

    private static void copyBeanProperties(Object target, Properties props, String propPrefix){
        PropertyAccessor myAccessor = PropertyAccessorFactory.forBeanPropertyAccess(target);
        props.forEach((k,v) -> {
            String beanProp = k.toString();
            if(propPrefix != null && k.toString().startsWith(propPrefix)){
                beanProp = beanProp.substring(propPrefix.length());
            }
            try {
                myAccessor.setPropertyValue(beanProp, v);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        });

    }
}
