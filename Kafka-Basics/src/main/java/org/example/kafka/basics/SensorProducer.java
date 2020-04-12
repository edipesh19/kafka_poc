package org.example.kafka.basics;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class SensorProducer {
    public static void main(String[] args) throws Exception{

        String topicName = "SensorTopic";

        Properties props = new Properties();
        props.put("bootstrap.servers", "den02cdl.us.oracle.com:9092,den02bno.us.oracle.com:9092");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("partitioner.class", "org.example.kafka.basics.SensorPartitioner");
        props.put("speed.sensor.name", "TSS");

        Producer<String, String> producer = new KafkaProducer<>(props);

        int j = 0;
        while(j < 1000000) {
            for (int i = 0; i < 10; i++)
                producer.send(new ProducerRecord<>(topicName, "SSP" + i, "500" + i));

            for (int i = 0; i < 10; i++)
                producer.send(new ProducerRecord<>(topicName, "TSS", "500" + i));
            j++;
            Thread.sleep(100);
        }
        producer.close();

        System.out.println("SimpleProducer Completed.");
    }
}
