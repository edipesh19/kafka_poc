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
public class KafkaFipsConfig {
    private String  sslKeyManagerAlgo;
    private String sslTrustManagerAlgo;
    private String securityProviders;
    private String sslEnabledProtocols;
}
