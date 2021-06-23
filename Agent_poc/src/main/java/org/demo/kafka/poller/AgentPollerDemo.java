package org.demo.kafka.poller;

import org.dipesh.kafka.client.AdminUtil;
import org.dipesh.kafka.client.KafkaClientConfig;
import org.dipesh.kafka.client.KafkaMessageFetcher;

import java.util.Properties;

public class AgentPollerDemo {
    public static Properties prop = new Properties();

    static {
        prop.setProperty("bootstrap.servers", "localhost:9092"); // AM
        prop.setProperty("auto.offset.reset", "earliest");
        prop.setProperty("enable.auto.commit", "false");
        prop.setProperty("session.timeout.ms", "30000");
        prop.setProperty("heartbeat.interval.ms", "1000");
        prop.setProperty("heartbeat.interval.ms", "1000");
        prop.setProperty("fetch.max.bytes", "10485760"); //AM
        prop.setProperty("max.partition.fetch.bytes", "10485760"); // AM
    }

    private KafkaClientConfig kafkaClientConfig = KafkaClientConfig.getInstance();
    private KafkaMessageFetcher messageFetcher;
    private String topicName;

    public AgentPollerDemo(String topicName) {
        this.topicName = topicName;
        kafkaClientConfig.setConsumerConfig(prop);
        messageFetcher = new KafkaMessageFetcher(AdminUtil.getGroupId(topicName),
            AdminUtil.getClientId(topicName), topicName, 4000, new MessageHandler());
    }

    public void startPoller() throws InterruptedException {
        Thread t = new Thread(messageFetcher);
        t.setName("Message Poller");
        t.start();
        t.join();
    }

    public static void main(String[] args) {
        AgentPollerDemo agentPollerDemo = new AgentPollerDemo(args[0]);

        try {
            agentPollerDemo.startPoller();
        } catch (InterruptedException e) {
            System.out.println("Caught exception "+ e.getMessage());
        }

        Runtime.getRuntime().addShutdownHook( new Thread( () -> {
            System.out.println("Exiting application");
            KafkaMessageFetcher.closePoller();
        }
        ));
    }


}
