package org.example.consistence.hashing;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.consistence.hashing.list.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

@SpringBootApplication
public class SBClient implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(SBClient.class);

    private final String[] servers = {"192.168.0.0", "192.168.0.1", "192.168.0.2", "192.168.0.3",
        "192.168.0.4"};
    private Map<String, Integer> distribution = null;

    @Autowired
    private ObservableList list;
    @Autowired
    private KeyManager keyManager;


    public static void main(String[] args) {
        logger.info("STARTING THE APPLICATION");
        SpringApplication.run(SBClient.class, args);
        logger.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) throws Exception {
        int numberofString = 100;
        List<String> keys = new ArrayList<>();
        int totalRun = 5;

        for (int i = 0; i < servers.length; i++) {
            list.add(servers[i]);
        }

        for (int i = 0; i < numberofString; i++) {
            keys.add(RandomStringUtils.randomAlphabetic(10));
        }

        for (int j = 0; j < totalRun; j++) {
            // get the server mapping for each string
            distribution = new HashMap<>();
            for (int i = 0; i < numberofString; i++) {
                String ip = keyManager.get(keys.get(i));
                //client.logger.info("{}   | {} | {}", i, keys.get(i), ip);
                if (distribution.containsKey(ip)) {
                    int val = distribution.get(ip);
                    distribution.put(ip, val + 1);
                } else {
                    distribution.put(ip, 1);
                }
            }

            for (Entry e : distribution.entrySet()) {
                logger.info("{} -> {}", e.getKey(), e.getValue());
            }

            int index = Math.abs(new Random().nextInt() % servers.length);
            logger.info("Removing {} from cluster", servers[index]);
            list.remove(servers[index]);

            // get the server mapping for each string
            distribution = new HashMap<>();
            for (int i = 0; i < numberofString; i++) {
                String ip = keyManager.get(keys.get(i));
                //client.logger.info("{}   | {} | {}", i, keys.get(i), ip);
                if (distribution.containsKey(ip)) {
                    int val = distribution.get(ip);
                    distribution.put(ip, val + 1);
                } else {
                    distribution.put(ip, 1);
                }
            }

            for (Entry e : distribution.entrySet()) {
                logger.info("{} -> {}", e.getKey(), e.getValue());
            }
            list.add(servers[index]);

            logger.info("************************************");
            logger.info("COMPLETED ITERATION {} of {}", j + 1, totalRun);
        }
    }
}
