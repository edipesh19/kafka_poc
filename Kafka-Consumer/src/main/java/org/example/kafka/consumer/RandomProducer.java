package org.example.kafka.consumer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

public class RandomProducer {
    public static void main(String[] args) throws InterruptedException{

        String topicName = "RandomProducerTopic";
        String msg;

        Properties props = new Properties();
        props.put("bootstrap.servers", "den02cdl.us.oracle.com:9092,den02bno.us.oracle.com:9092");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        Random rg = new Random();
        Calendar dt = Calendar.getInstance();
        dt.set(2016,1,1);
        try{
            while(true){
                for (int i=0;i<100;i++){
                    msg = dt.get(Calendar.YEAR)+"-"+dt.get(Calendar.MONTH)+"-"+dt.get(Calendar.DATE) + "," + rg.nextInt(1000);
                    producer.send(new ProducerRecord<String, String>(topicName,0,"Key",msg)).get();
                    msg = dt.get(Calendar.YEAR)+"-"+dt.get(Calendar.MONTH)+"-"+dt.get(Calendar.DATE) + "," + rg.nextInt(1000);
                    producer.send(new ProducerRecord<String, String>(topicName,1,"Key",msg)).get();
                }
                dt.add(Calendar.DATE,1);
                System.out.println("Data Sent for " + dt.get(Calendar.YEAR) + "-" + dt.get(Calendar.MONTH) + "-" + dt.get(Calendar.DATE) );
            }
        }
        catch(Exception ex){
            System.out.println("Intrupted");
        }
        finally{
            producer.close();
        }

    }
}

