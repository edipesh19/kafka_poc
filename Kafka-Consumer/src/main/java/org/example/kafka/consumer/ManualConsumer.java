package org.example.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ManualConsumer {

    private static String KAFKA_SERVER = "localhost:9092";
    Date date = new Date();
    Map<String, Consumer<String, String>> consumerCache = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {

        long sleepInterval = 0;
        String topicName = "mytopic12";

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

    public void consume(int number){
        if(number < 0) {
            while (true) {
                System.out.println("========================================");
                readRecords("mytopic12", "GRP-ManualConsumerGroup15", "ConsumerId-15");
            }
        } else {
            for(int i = 0; i < number; i++) {
                System.out.println("========================================");
                readRecords("mytopic12", "GRP-ManualConsumerGroup15", "ConsumerId-15");
            }
        }
    }

    public static void log(String message) {
        System.out.println(message);
    }

    public static void printUsage() {
        System.out.println("USAGE : java -jar <consumer.jar> (topicName) [sleep interval]");
    }

    private void readRecords(String topicName, String groupName, String consumerId) {

        try {
            Consumer<String, String> consumer = consumerCache.get(topicName);
            if (consumer == null) {
                consumer = getConsumer(groupName, consumerId, topicName);
                consumerCache.put(topicName, consumer);
            }

            long startTime = System.currentTimeMillis();
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(200));

            System.out.println("Time taken to poll = " + (System.currentTimeMillis()- startTime) + " ms");
            // stop and calculate the time
            if (records != null && !records.isEmpty()) { // Get the first record.
                //ConsumerRecord<String, String> rec = records.iterator().next();
                for (ConsumerRecord<String, String> rec : records) {
                System.out.println(records.partitions().iterator().next() + " . " + rec.key() + " -> " + rec.value());

                // Set the commit offset to the next offset for subsequent polls.
                }
                consumer.commitSync();
            } else {
                System.out.println("No record found "  + records.count());
            }
        } catch (Exception ex) {
            System.out.println("Consumer Exception because " + ex.getMessage());
            throw ex;
        }
    }

    public Consumer<String, String> getConsumer(String consumerGroupId, String consumerId, String topicName) {
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
        consumerProps.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");

        Consumer<String, String> consumer = new KafkaConsumer<>(consumerProps);

        consumer.assign(Collections.singletonList(new TopicPartition(topicName, 0)));
        return consumer;
    }
}

