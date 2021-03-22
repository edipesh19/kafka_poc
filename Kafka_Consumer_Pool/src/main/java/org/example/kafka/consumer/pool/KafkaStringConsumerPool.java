package org.example.kafka.consumer.pool;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class KafkaStringConsumerPool {
    private static Logger LOG = LoggerFactory
        .getLogger(KafkaStringConsumerPool.class);

    private KafkaConsumerPool<String, String> stringStringKafkaConsumerPool;

    @Autowired
    public KafkaStringConsumerPool(KafkaConsumerPool<String, String> stringStringKafkaConsumerPool) {
        this.stringStringKafkaConsumerPool = stringStringKafkaConsumerPool;
        LOG.info("========================= KafkaStringConsumerPool =========================");
    }

    @PostConstruct
    void init(){
        stringStringKafkaConsumerPool.setConsumerProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        stringStringKafkaConsumerPool.setConsumerProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        stringStringKafkaConsumerPool.setConsumerProperty("enable.auto.commit", false);
        LOG.info("========================= KafkaStringConsumerPool init =========================");
    }

    public KafkaConsumerPool<String, String> getStringStringKafkaConsumerPool() {
        return stringStringKafkaConsumerPool;
    }
}
