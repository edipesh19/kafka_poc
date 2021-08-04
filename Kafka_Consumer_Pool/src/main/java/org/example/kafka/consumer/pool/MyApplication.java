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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class MyApplication implements CommandLineRunner {

        private static Logger LOG = LoggerFactory
            .getLogger(MyApplication.class);

        private final String topicName = "myTopic1";


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
            Thread t1 = new Thread(new ExecuteCall(1, kafkaStringConsumerPool), "T1");
            Thread t2 = new Thread(new ExecuteCall(10, kafkaStringConsumerPool), "T2");
            Thread t3 = new Thread(new ExecuteCall(20, kafkaStringConsumerPool), "T3");
            Thread t4 = new Thread(new ExecuteCall(30, kafkaStringConsumerPool), "T4");
            LOG.info("STARTING T1");
            t1.start();
            LOG.info("STARTING T2");
            t2.start();
            LOG.info("STARTING T3");
            t3.start();
            LOG.info("STARTING T4");
            t4.start();

            LOG.info("JOIN T1");
            t1.join();
            LOG.info("JOIN T2");
            t2.join();
            LOG.info("JOIN T3");
            t3.join();
            LOG.info("JOIN T4");
            t4.join();

            Thread.sleep(8000);
            LOG.info("EXITING MAIN");
        }
}
