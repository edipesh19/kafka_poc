package org.dipesh.kafka.client;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class AdminUtil {
    private static KafkaClientConfig kafkaClientConfig = KafkaClientConfig.getInstance();

    /**
     * @param consumerGroupId consumerGroupId for the kafka consumer
     * @param consumerId      consumerId for the kafka consumer
     * @return a kafka consumer for "forAgent" topic
     */
    public static Consumer<String, String> getRequestTopicConsumer(String consumerGroupId, String consumerId) {

        Properties consumerProps = kafkaClientConfig.getConsumerConfig();

        consumerProps.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        consumerProps.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, consumerId);
        consumerProps.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // NOTE: Given that Agent accepts a single message at a time, fetching 1 record would help in
        // optimizing resource consumption w.r.t. fetching multiple messages
        consumerProps.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");

        // We are creating single partitioned topic hence the consumer is assigned to partition 0.
        // With multi-partitioned topic we face frequent Re-balancing issue in kafka, cause delay in
        // reading data from kafka, because we create and close the consumer in every request.
        return new KafkaConsumer<>(consumerProps);
    }

    public static String getGroupId(String topicName) {
        return "Grp-" + getClientId(topicName);
    }

    public static String getClientId(String topicName) {
        return "cnsr" + "-" + topicName + "-" + "01";
    }
}
