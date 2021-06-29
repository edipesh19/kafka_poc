package org.example.consistence.hashing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    Logger logger = LoggerFactory.getLogger(Client.class);

    private String[] servers = { "192.168.0.0:111", "192.168.0.1:111",
        "192.168.0.2:111", "192.168.0.3:111", "192.168.0.4:111" };

    public static void main(String[] args) {
        Client client = new Client();

        ConsistenceHashing ch = new ConsistenceHashing();
        for (int i=0; i<client.servers.length; i++) {
            ch.put(client.servers[i]);
        }

        String[] keys = {"sunlight", "Moon", "Stars"};
        for(int i=0; i<keys.length; i++){
            client.logger.info("{} routed to {}", keys[i], ch.get(keys[i]));
        }
    }
}
