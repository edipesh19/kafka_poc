package org.example.kafka.basics;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class SimpleProducer {
    public static void main(String[] args) throws Exception{

        String topicName = "mytopic12";
        String key;
        String value;

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);

        ProducerRecord<String, String> record;
        for(int i=0;i<100000;i++) {
            key = "Key" + i;
            value = "Message-" + i;
            record = new ProducerRecord<>(topicName,key,value);
            producer.send(record);
        }
        producer.close();

        System.out.println("SimpleProducer Completed.");
    }
}
