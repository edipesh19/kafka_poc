package org.dipesh.kafka.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KafkaSslConfig {
    private String sslKeystoreLocation;
    private String sslKeystorePassword;
    private String sslTruststoreLocation;
    private String sslTruststorePassword;
    private String sslKeyPassword;
    private String sslEndpointIdentificationAlgo;
    private String securityProtocolConfig;
    private String sslKeystoreType;
    private String sslTruststoreType;
}
