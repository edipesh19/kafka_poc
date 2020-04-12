package org.example.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

public class ManualConsumer {

    private static String KAFKA_SERVER = "server1:9092,server2:9092,server3:9092";

    public static void main(String[] args) throws Exception {

        long sleepInterval = 0;
        String topicName = "mytopic";

        if (args.length == 2) {
            topicName = args[0];
            sleepInterval = args[1] == null ? 0L : Long.parseLong(args[1]);
        }

        String groupName = "GRP-ManualConsumerGroup15";
        String consumerId = "ConsumerId-15";

        log("Topic name is : " + topicName);
        log("Sleep Interval = " + sleepInterval);
        log("Group Name : " + groupName);
        log("Consumer Id : " + consumerId);


        ManualConsumer manualConsumer = new ManualConsumer();

        while (true) {
            System.out.println("========================================");
            manualConsumer.readRecords(topicName, groupName, consumerId);
        }
    }

    public static void log(String message) {
        System.out.println(message);
    }

    public static void printUsage() {
        System.out.println("USAGE : java -jar <consumer.jar> (topicName) [sleep interval]");
    }

    private void readRecords(String topicName, String groupName, String consumerId) {
        Consumer<String, String> consumer = getConsumer(groupName, consumerId);
        consumer.subscribe(Pattern.compile(topicName));

        try {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(5000));
            if (records != null && !records.isEmpty()) { // Get the first record.
                //ConsumerRecord<String, String> rec = records.iterator().next();
                for (ConsumerRecord<String, String> rec : records) {
                System.out.println(records.partitions().iterator().next() + " . " + rec.key() + " -> " + rec.value());

                // Set the commit offset to the next offset for subsequent polls.
                }
                consumer.commitSync();
            }
            consumer.close();
        } catch (Exception ex) {
            System.out.println("Consumer Exception because " + ex.getMessage());
            throw ex;
        }
    }

    public Consumer<String, String> getConsumer(String consumerGroupId, String consumerId) {
        Properties consumerProps = new Properties();
        consumerProps.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        consumerProps.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        consumerProps.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, consumerId);
        consumerProps.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        //consumerProps.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "3000");
        consumerProps.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        consumerProps.setProperty(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "1000");
        consumerProps.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "5");

        return new KafkaConsumer<>(consumerProps);
    }
}

