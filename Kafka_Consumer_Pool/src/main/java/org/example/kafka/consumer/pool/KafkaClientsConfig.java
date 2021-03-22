package org.example.kafka.consumer.pool;


import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SecurityConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "kafka", ignoreInvalidFields = true)
public class KafkaClientsConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaClientsConfig.class);
    private static volatile KafkaClientsConfig clientConfigInstance;

    private static final String BOOTSTRAP_SERVERS_CONFIG = AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
    private static final String SSL_KEYSTORE_LOCATION = SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG;
    private static final String SSL_KEYSTORE_PASSWORD = SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG;
    private static final String SSL_TRUSTSTORE_LOCATION = SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG;
    private static final String SSL_TRUSTSTORE_PASSWORD = SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG;
    private static final String SECURITY_PROTOCOL_CONFIG = AdminClientConfig.SECURITY_PROTOCOL_CONFIG;
    private static final String SSL_KEY_PASS = SslConfigs.SSL_KEY_PASSWORD_CONFIG;
    private static final String SSL_ENDPOINT_IDENTIFICATION_ALGO = "ssl.endpoint.identification.algorithm";
    private static final String SSL_KEYMANAGER_ALGORITHM = SslConfigs.SSL_KEYMANAGER_ALGORITHM_CONFIG;
    private static final String SSL_TRUSTMANAGER_ALGORITHM = SslConfigs.SSL_TRUSTMANAGER_ALGORITHM_CONFIG;
    private static final String SSL_KEYSTORE_TYPE = SslConfigs.SSL_KEYSTORE_TYPE_CONFIG;
    private static final String SSL_TRUSTSTORE_TYPE = SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG;
    private static final String SSL_PROTOCOL = SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG;
    private static final String SECURITY_PROVIDERS = SecurityConfig.SECURITY_PROVIDERS_CONFIG;
    public static final String USE_HTTPS_PROP = "secure.use.ssl";
    public static final String FIPS_ENABLED = "FIPS_ON";

    private Properties adminClientConfig = new Properties();
    private Map<String, String> topicConfig = new HashMap<>();
    private Properties producerConfig = new Properties();
    private Properties consumerConfig = new Properties();
    private String bootstrapServers = "localhost:9092";
    private Integer topicNumPartitions = 1;
    private Short   topicReplicationFactor =  2;
    private String  metadataMaxAgeMs;
    private String sslKeystoreLocation;
    private String sslKeystorePassword;
    private String sslTruststoreLocation;
    private String sslTruststorePassword;
    private String sslKeyPassword;
    private String sslEndpointIdentificationAlgo;
    private String securityProtocolConfig;
    private String sslKeyManagerAlgo;
    private String sslTrustManagerAlgo;
    private String sslKeystoreType;
    private String sslTruststoreType;
    private String sslEnabledProtocols;
    private String securityProviders;

    private int sslEnabled = -1;
    private int fipsEnabled = -1;

    @Autowired
    private Environment env;

    private KafkaClientsConfig() {
        synchronized (this) {
            clientConfigInstance = this;
        }
    }

    public static KafkaClientsConfig getInstance() {
        return clientConfigInstance;
    }

    public Properties getAdminClientConfig() {
        return setConfigProperties(this.adminClientConfig);
    }

    public Properties getProducerConfig() {
        return setConfigProperties(this.producerConfig);
    }

    public Properties getConsumerConfig() {
        consumerConfig.put("auto.offset.reset", "earliest");
        consumerConfig.put("enable.auto.commit", "false");
        consumerConfig.put("session.timeout.ms", "30000");
        consumerConfig.put("heartbeat.interval.ms", "1000");
        consumerConfig.put("fetch.max.bytes", "10485760");
        consumerConfig.put("max.partition.fetch.bytes", "10485760");
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return setConfigProperties(this.consumerConfig);
    }

    private Properties setConfigProperties(Properties props) {
        props.setProperty(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return props;
    }
}
