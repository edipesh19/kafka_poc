package org.example.kafka.consumer.pool;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;

import java.time.Duration;
import java.util.Collections;

@SpringBootApplication
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class MyApplication implements CommandLineRunner {

        private static Logger LOG = LoggerFactory
            .getLogger(MyApplication.class);

        private final String topicName = "mytopic";


        @Autowired
        private KafkaStringConsumerPool kafkaStringConsumerPool;

        private PoolableConsumer<String, String> consumer;

        public static void main(String[] args) {
            LOG.info("STARTING THE APPLICATION");
            SpringApplication.run(MyApplication.class, args);
            LOG.info("APPLICATION FINISHED");
        }

        @Override
        public void run(String... args) throws InterruptedException {
            LOG.info("EXECUTING : command line runner");
            //kafkaStringConsumerPool = new KafkaStringConsumerPool();
            while (true) {
                try {
                    consumer = kafkaStringConsumerPool.getStringStringKafkaConsumerPool().borrowObject();
                    consumer.assign(Collections.singletonList(new TopicPartition(topicName, 0)));
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
                    if (records != null && !records.isEmpty()) {
                        for (ConsumerRecord<String, String> rec : records) {
                            System.out.println(records.partitions().iterator().next() + " . " + rec.key() + " -> " + rec.value());
                        }
                        consumer.commitSync();
                    } else {
                        System.out.println("No record found " + records.count());
                    }
                } catch (Exception e) {
                    LOG.info("Caught exception in polling for record ", e);
                } finally {
                    LOG.info("Releasing consumer to the pool");
                    kafkaStringConsumerPool.getStringStringKafkaConsumerPool().release(consumer);
                }
                Thread.sleep(10000);
            }
        }
}
