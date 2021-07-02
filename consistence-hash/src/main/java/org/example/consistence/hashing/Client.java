package org.example.consistence.hashing;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class Client {
    Logger logger = LoggerFactory.getLogger(Client.class);

    private String[] servers = { "192.168.0.0", "192.168.0.1", "192.168.0.2", "192.168.0.3",
        "192.168.0.4"};
    private Map<String, Integer> distribution = null;

    public static void main(String[] args) {
        int numberofString = 100;
        List<String> strings = new ArrayList<>();
        Client client = new Client();
        int totalRun = 5;

        ConsistenceHashing ch = new ConsistenceHashing();
        for (int i=0; i<client.servers.length; i++) {
            ch.put(client.servers[i]);
        }

        for(int i = 0; i < numberofString; i++){
            strings.add(RandomStringUtils.randomAlphabetic(10));
        }

        for(int j = 0; j < totalRun; j++) {
            // get the server mapping for each string
            client.distribution = new HashMap<>();
            for (int i = 0; i < numberofString; i++) {
                String ip = ch.get(strings.get(i));
                //client.logger.info("{}   | {} | {}", i, strings.get(i), ip);
                if (client.distribution.containsKey(ip)) {
                    int val = client.distribution.get(ip);
                    client.distribution.put(ip, val + 1);
                } else {
                    client.distribution.put(ip, 1);
                }
            }

            for (Entry e : client.distribution.entrySet()) {
                client.logger.info("{} -> {}", e.getKey(), e.getValue());
            }

            int index = Math.abs(new Random().nextInt()% client.servers.length);
            client.logger.info("Removing {} from cluster", client.servers[index]);
            ch.remove(client.servers[index]);

            // get the server mapping for each string
            client.distribution = new HashMap<>();
            for (int i = 0; i < numberofString; i++) {
                String ip = ch.get(strings.get(i));
                //client.logger.info("{}   | {} | {}", i, strings.get(i), ip);
                if (client.distribution.containsKey(ip)) {
                    int val = client.distribution.get(ip);
                    client.distribution.put(ip, val + 1);
                } else {
                    client.distribution.put(ip, 1);
                }
            }

            for (Entry e : client.distribution.entrySet()) {
                client.logger.info("{} -> {}", e.getKey(), e.getValue());
            }
            ch.put(client.servers[index]);

            client.logger.info("************************************");
            client.logger.info("COMPLETED ITERATION {} of {}",j+1, totalRun);
        }
    }
}
