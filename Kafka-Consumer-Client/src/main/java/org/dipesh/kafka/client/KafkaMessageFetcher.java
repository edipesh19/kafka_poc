package org.dipesh.kafka.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.dicom.agent.mediator.dto.AgentMessageDTO;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class KafkaMessageFetcher implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageFetcher.class.getName());

    private static ObjectMapper c_objMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final String topicName;
    private final long topicPollTimeoutMs;
    private final AtomicLong messageCacheReadingGauge;
    private final static AtomicBoolean isClosed = new AtomicBoolean(false);
    private final IMessageHandler messageHandler;
    private final String consumerGroupId;
    private final String consumerClientId;

    public KafkaMessageFetcher(String consumerGroupId, String consumerClientId, String topicName,
                               long topicPollTimeoutMs, IMessageHandler messageHandler) {
        this.consumerGroupId = consumerGroupId;
        this.consumerClientId = consumerClientId;
        this.topicName = topicName;
        this.topicPollTimeoutMs = topicPollTimeoutMs;
        this.messageHandler = messageHandler;
        this.messageCacheReadingGauge = new AtomicLong(0L);
    }


    @Override
    public void run() {
        try (Consumer<String, String> consumer =
                 AdminUtil.getRequestTopicConsumer(this.consumerGroupId, this.consumerClientId)) {
            logger.debug("Created consumer for topic: {}", topicName);

            // Assign consumer to topic.
            consumer.assign(Collections.singletonList(new TopicPartition(topicName, 0)));

            do {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(this.topicPollTimeoutMs));

                    messageCacheReadingGauge.getAndSet(1L);
                    if (records != null && !records.isEmpty()) {
                        logger.debug("No. of records obtained: {}", records.count());
                        messageCacheReadingGauge.getAndSet(records.count());

                        for (ConsumerRecord<String, String> rec : records) {
                            if (rec != null) {
                                String messageString = rec.value();
                                System.out.println("Rec offset = " + rec.offset());
                                if (messageString != null) {
                                    messageHandler.handleMessage(c_objMapper.readValue(messageString, AgentMessageDTO.class));
                                } else {
                                    logger.warn("Message is null for key {} and topic {}", rec.key(), rec.topic());
                                }
                            }
                        }
                        consumer.commitSync();
                    }
                } catch (Exception ex) {
                    logger.error("Caught exception in from agent topic consumer cause {}", ex.getMessage(), ex);
                    messageCacheReadingGauge.getAndSet(0L);
                }
            } while (isClosed.get() != true);

        } catch (WakeupException e) {
            logger.info("Caught WakeupException {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Caught exception during consuming topic:" + topicName, e);
            messageCacheReadingGauge.getAndSet(-1L);
        }
    }

    public static void closePoller() {
        isClosed.getAndSet(true);
    }
}
