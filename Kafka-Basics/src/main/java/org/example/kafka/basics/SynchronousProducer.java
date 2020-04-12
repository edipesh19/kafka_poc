package org.example.kafka.basics;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

public class SynchronousProducer {

    private static String KAFKA_SERVER = "den02cdl.us.oracle.com:9092,den02bno.us.oracle.com:9092,den02qfw.us.oracle.com:9092";

    public static void main(String[] args) throws Exception {

        long sleepInterval = 0;
        if (args.length == 0) {
            printUsage();
        } else if (args.length == 2) {
            sleepInterval = args[1] == null ? 0L : Long.parseLong(args[1]);
        }

        final String topicName = args[0];


        log("Topic name is : " + topicName);
        log("Sleep Interval = " + sleepInterval);

        SynchronousProducer synchronousProducer = new SynchronousProducer();

        for (int i = 1; ; i++) {
            String key = "Key-" + i;
            String val = "Value-" + i;
            synchronousProducer.publishRecord(topicName, key, val);
            //Thread.sleep(sleepInterval);
        }

    }

    public static void log(String message) {
        System.out.println(message);
    }

    public static void printUsage() {
        System.out.println("USAGE : java -jar <producer.jar> (topicName) [sleep interval]");
    }

    private void publishRecord(String topicName, String key, String value) {
        Producer<String, String> producer = getProducer();

        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, value);

        try {
            RecordMetadata metadata = producer.send(record).get();
            System.out.println("Partition " + metadata.partition() + "| offset " + metadata.offset() + "| key" +  key + "| Value = " + value);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SynchronousProducer failed with an exception");
        } finally {
            producer.close();
        }
    }

    private Producer<String, String> getProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "3000");
        props.put(ProducerConfig.RETRIES_CONFIG, "2");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "2");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        return new KafkaProducer<>(props);
    }

}

