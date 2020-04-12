package org.example.kafka.basics;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

public class AsynchronousProducer {
    public static void main(String[] args) throws Exception{
        String topicName = "newhat";
        String key = "Key1";
        String value = "Asyns-Value-1";

        Properties props = new Properties();
        props.put("bootstrap.servers", "den02cdl.us.oracle.com:9092,den02bno.us.oracle.com:9092");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        Producer<String, String> producer = new KafkaProducer<>(props);

        ProducerRecord<String, String> record = new ProducerRecord<>(topicName,key,value);

        producer.send(record, new MyProducerCallback());
        System.out.println("AsynchronousProducer call completed");
        producer.close();

    }
}
class MyProducerCallback implements Callback {

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception != null)
            System.out.println("AsynchronousProducer failed with an exception");
        else
            System.out.println("AsynchronousProducer call Success:");
    }
}