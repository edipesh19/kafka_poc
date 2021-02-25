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

public class ManualConsumerWithPlayPause {

    private static String KAFKA_SERVER = "localhost:9092";

    public static void main(String[] args) throws Exception {

        long sleepInterval = 0;
        String topicName = "mytopic1";

        int sleeptime = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        //boolean commitFlag = Boolean.parseBoolean(args[2]);
        //boolean exitflag = Boolean.parseBoolean(args[3]);

        if (args.length == 6) {
            topicName = args[2];
            sleepInterval = args[3] == null ? 0L : Long.parseLong(args[1]);
        }

        String groupName = "GRP-ManualConsumerGroup16";
        String consumerId = "ConsumerId16";

        log("Topic name is : " + topicName);
        log("Sleep Interval = " + sleepInterval);
        log("Group Name : " + groupName);
        log("Consumer Id : " + consumerId);


        ManualConsumerWithPlayPause manualConsumerWithPlayPause = new ManualConsumerWithPlayPause();

       for (int i=0;i<n;i++) {
            //System.out.println("========================================");
            manualConsumerWithPlayPause.readRecords(topicName, groupName, consumerId, sleeptime);
        }
    }

    public static void log(String message) {
        System.out.println(message);
    }

    public static void printUsage() {
        System.out.println("USAGE : java -jar <consumer.jar> (topicName) [sleep interval]");
    }

    private void readRecords(String topicName, String groupName, String consumerId, int sleeptime) throws InterruptedException {

        try (Consumer<String, String> consumer = getConsumer(groupName, consumerId)) {

        consumer.subscribe(Pattern.compile(topicName));

//        Collection<TopicPartition> topicPartitions = new ArrayList<>();
//        TopicPartition topicPartition = new TopicPartition(topicName, 0);
//        topicPartitions.add(topicPartition);
//        consumer.assign(topicPartitions);

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
        if (records != null && !records.isEmpty()) { // Get the first record.
            //ConsumerRecord<String, String> rec = records.iterator().next();
            //consumer.pause(topicPartitions);
            for (ConsumerRecord<String, String> rec : records) {
                System.out.println(records.partitions().iterator().next() + " . " + rec.key() + " -> " + rec.value());
                System.out.println("Offset" + rec.offset());

                // Set the commit offset to the next offset for subsequent polls.
            }
            Thread.sleep(sleeptime);
            System.out.println("Committing");
            consumer.commitSync();
//            System.out.println("Closing");
//            consumer.close();
                //commitOffsetForInstanceTopic(groupName, consumerId, topicName, sleeptime);
            //Thread.sleep(sleeptime);
            //consumer.resume(topicPartitions);
        }
        } catch (Exception ex) {
            System.out.println("Consumer Exception because " + ex.getMessage());
            throw ex;
        }
    }

    public void commitOffsetForInstanceTopic(String groupName, String consumerId, String topicName, int sleeptime) {
        Consumer<String, String> consumer = getConsumer(groupName, consumerId);
        consumer.subscribe(Pattern.compile(topicName));
        /*Collection<TopicPartition> topicPartitions = new ArrayList<>();
        topicPartitions.add(new TopicPartition(topicName, 0));
        consumer.assign(topicPartitions);*/
        try {

            long currOffset = consumer.position(new TopicPartition(topicName,0));

            Thread.sleep(sleeptime);

            // We are creating single partitioned topic hence the consumer is assigned to partition 0.
            // So after reading from partition 0, commit the offset to the next position for partitioned 0.
            consumer.commitSync(
                Collections.singletonMap(new TopicPartition(topicName, 0), new OffsetAndMetadata(currOffset + 1))
            );

            // Closing consumer will trigger re-balancing in kafka for multi-partitioned topics
            consumer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
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
        consumerProps.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "6000");
        consumerProps.setProperty(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "1000");
        consumerProps.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        return new KafkaConsumer<>(consumerProps);
    }
}

