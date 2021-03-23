package org.apache.kafka.consumer.cache;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KafkaStringConsumerCache {
    private static final Logger log = LoggerFactory.getLogger(KafkaStringConsumerCache.class);

    Map<String, CacheableConsumer<String, String>> consumerCache;

    CacheableConsumerFactory<String, String> kafkaStringConsumerFactory;

    @Autowired
    public KafkaStringConsumerCache(CacheableConsumerFactory<String, String> kafkaStringConsumerFactory) {
        this.kafkaStringConsumerFactory = kafkaStringConsumerFactory;
        consumerCache = new ConcurrentHashMap<>();
    }

    public CacheableConsumer<String, String> getConsumer(String topicName) throws Exception {
        if(!consumerCache.containsKey(topicName)){
            Properties consumerProp = getConsumer("Grp-" + topicName, topicName);
            consumerCache.put(topicName, kafkaStringConsumerFactory.create(consumerProp));
        }
        return consumerCache.get(topicName);
    }

    public Properties getConsumer(String consumerGroupId, String consumerId) {
        Properties consumerProps = new Properties();
        consumerProps.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        consumerProps.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, consumerId);
        consumerProps.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProps.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        consumerProps.setProperty(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "1000");
        consumerProps.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        return consumerProps;
    }

}
