package org.example.kafka.consumer.pool;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ExecuteCall implements Runnable{
    private static Logger LOG = LoggerFactory
            .getLogger(ExecuteCall.class);
    int n;
    private PoolableConsumer<String, String> consumer;
    private KafkaStringConsumerPool kafkaStringConsumerPool;
    private final String topicName = "myTopic1";

    public ExecuteCall(int n, KafkaStringConsumerPool kafkaStringConsumerPool) {
        this.n = n;
        this.kafkaStringConsumerPool = kafkaStringConsumerPool;
    }

    @Override
    public void run() {
        LOG.info("EXECUTING : command line runner");

        for(long offset = n; offset < n+1; offset++ ) {
            try {
                Map<TopicPartition, Long> off = new HashMap<>();
                off.put(new TopicPartition(topicName, 0), offset);
                consumer = kafkaStringConsumerPool.getStringKafkaConsumerPool()
                        .acquire(1000, TimeUnit.MILLISECONDS, off);
                if(consumer != null) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
                    if (records != null && !records.isEmpty()) {
                        for (ConsumerRecord<String, String> rec : records) {
                            LOG.info(Thread.currentThread().getName() + " OFFSET = " + rec.offset());
                            if (rec.offset() == offset) {
                                LOG.info(Thread.currentThread().getName() + " Dipesh " + records.partitions().iterator().next() + " . " + rec.key() + " -> " + rec.value());
                            }
                        }
                        //consumer.commitSync();
                    } else {
                        LOG.info(Thread.currentThread().getName() + " No record found " + records.count());
                    }
                }
            } catch (Exception e) {
                LOG.info(Thread.currentThread().getName() + " Caught exception in polling for record ", e);
            } finally {
                LOG.info(Thread.currentThread().getName() + " Releasing consumer to the pool");
                if(consumer != null) {
                    kafkaStringConsumerPool.getStringKafkaConsumerPool().release(consumer);
                }
                consumer = null;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
