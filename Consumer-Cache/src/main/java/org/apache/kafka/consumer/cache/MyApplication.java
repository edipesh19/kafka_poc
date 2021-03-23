package org.apache.kafka.consumer.cache;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;

@SpringBootApplication
public class MyApplication implements CommandLineRunner {
    private static Logger LOG = LoggerFactory.getLogger(MyApplication.class);

    @Autowired
    private KafkaStringConsumerCache consumerCache;

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(MyApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    public void run(String... args) throws InterruptedException {
        LOG.info("EXECUTING : command line runner");
        Thread t1 = new Thread(new Task("mytopic1"));
        Thread t2 = new Thread(new Task("mytopic2"));
        Thread t3 = new Thread(new Task("mytopic3"));

        t1.start();
        t2.start();
        t3.start();
        t1.join();t2.join();t3.join();

        LOG.info("FINISHED : command line runner");
    }

    class Task implements Runnable {

        private String topicName;

        public Task(String topicName) {
            this.topicName = topicName;
        }

        @Override
        public void run() {
            try {
                readRecords(consumerCache.getConsumer(topicName));
            } catch (Exception e) {
                LOG.error("Caught exception in creating consumer object");
            }
        }
    }
    private void readRecords(CacheableConsumer<String, String> consumer) {
        try {
            long startTime = System.currentTimeMillis();
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(2000));

            System.out.println("Time taken to poll = " + (System.currentTimeMillis()- startTime) + " ms");
            // stop and calculate the time
            if (records != null && !records.isEmpty()) { // Get the first record.
                //ConsumerRecord<String, String> rec = records.iterator().next();
                for (ConsumerRecord<String, String> rec : records) {
                    System.out.println(records.partitions().iterator().next() + " . " + rec.key() + " -> " + rec.value());
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
}
