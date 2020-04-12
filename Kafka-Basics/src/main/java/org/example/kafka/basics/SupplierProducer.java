package org.example.kafka.basics;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class SupplierProducer {
    public static void main(String[] args) throws Exception{

        String topicName = "SensorTopic";

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "den02cdl.us.oracle.com:9092,den02bno.us.oracle.com:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.example.kafka.basics.SupplierSerializer");

        Producer<String, Supplier> producer = new KafkaProducer<>(props);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Supplier sp1 = new Supplier(101,"Xyz Pvt Ltd.",df.parse("2016-04-01"));
        Supplier sp2 = new Supplier(102,"Abc Pvt Ltd.",df.parse("2012-01-01"));

        int i = 0;
        while(i < 100000) {
            producer.send(new ProducerRecord<String, Supplier>(topicName, "SUP", sp1)).get();
            producer.send(new ProducerRecord<String, Supplier>(topicName, "SUP", sp2)).get();
            i++;
            Thread.sleep(10);
        }

        System.out.println("SupplierProducer Completed.");
        producer.close();

    }
}
