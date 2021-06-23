package org.example.kafka.basics;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaTransactioalProducer {
    public static void main(String[] args) {
        String topicName = args[0];
        String key;
        String value;

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("enable.idempotence", "true");
        props.put("transactional.id", "prod-1");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);

        try{
            producer.initTransactions();
            ProducerRecord<String, String> record;
            for (int i = 0; i < 1000000; i++) {
                key = "Key" + i;
                value = "Message-" + i;
                System.out.println("Key = " + key + ", value = " + value);
                record = new ProducerRecord<>(topicName, key, value);
                producer.send(record);
            }
        }catch (Exception ex) {

        } finally {
            producer.close();
        }


        System.out.println("SimpleProducer Completed.");
    }
}
