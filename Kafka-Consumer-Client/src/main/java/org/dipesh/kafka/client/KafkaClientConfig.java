package org.dipesh.kafka.client;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.config.SecurityConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

@Getter
@Setter
public class KafkaClientConfig {
    private static final Logger logger = LoggerFactory.getLogger(KafkaClientConfig.class.getName());

    private static KafkaClientConfig clientConfigInstance = new KafkaClientConfig();

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

    private Properties consumerConfig = new Properties();

    public static KafkaClientConfig getInstance() {
        return clientConfigInstance;
    }

    public Properties setDefaultKafkaConsumerConfig(Properties props) {
        consumerConfig.putAll(props);
        return consumerConfig;
    }

    public Properties setSslKafkaConsumerConfig(KafkaSslConfig kafkaSslConfig) {
        consumerConfig.setProperty(SECURITY_PROTOCOL_CONFIG, kafkaSslConfig.getSecurityProtocolConfig());
        consumerConfig.setProperty(SSL_KEYSTORE_LOCATION, kafkaSslConfig.getSslKeystoreLocation());
        consumerConfig.setProperty(SSL_KEYSTORE_PASSWORD, kafkaSslConfig.getSslKeystorePassword());
        consumerConfig.setProperty(SSL_TRUSTSTORE_LOCATION, kafkaSslConfig.getSslTruststoreLocation());
        consumerConfig.setProperty(SSL_TRUSTSTORE_PASSWORD, kafkaSslConfig.getSslTruststorePassword());
        consumerConfig.setProperty(SSL_KEY_PASS, kafkaSslConfig.getSslKeyPassword());
        consumerConfig.setProperty(SSL_ENDPOINT_IDENTIFICATION_ALGO, kafkaSslConfig.getSslEndpointIdentificationAlgo());
        consumerConfig.setProperty(SSL_KEYSTORE_TYPE, kafkaSslConfig.getSslKeystoreType());
        consumerConfig.setProperty(SSL_TRUSTSTORE_TYPE, kafkaSslConfig.getSslTruststoreType());
        return consumerConfig;
    }

    public Properties setFipsKafkaConsumerConfig(KafkaFipsConfig kafkaFipsConfig) {
        consumerConfig.setProperty(SSL_KEYMANAGER_ALGORITHM, kafkaFipsConfig.getSslKeyManagerAlgo());
        consumerConfig.setProperty(SSL_TRUSTMANAGER_ALGORITHM, kafkaFipsConfig.getSslTrustManagerAlgo());
        consumerConfig.setProperty(SECURITY_PROVIDERS, kafkaFipsConfig.getSecurityProviders());
        consumerConfig.setProperty(SSL_PROTOCOL, kafkaFipsConfig.getSslEnabledProtocols());
        return consumerConfig;
    }
}
