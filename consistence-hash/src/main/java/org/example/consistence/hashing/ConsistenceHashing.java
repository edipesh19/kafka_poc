package org.example.consistence.hashing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class ConsistenceHashing {

    private static final int virtualNodeNum = 100;
    Logger logger = LoggerFactory.getLogger(ConsistenceHashing.class);
    //key represents the hash value of the server and value represents the server
    private final SortedMap<Integer, String> sortedMap = new TreeMap<Integer, String>();

    public String put(String data) {
        String ret = null;
        try {
            IntStream.range(0, virtualNodeNum)
                .mapToObj(i -> data + "&&VN" + i).forEach(vn -> {
                int hash = getHash(vn);
                logger.debug("{} -> {} -- Hash {}", data, vn, hash);
                sortedMap.put(hash, vn);
            });
            logger.info("Server [{}] added to the map", data);
        } catch (Exception ex) {
            logger.error("Caught Exception while adding entry {} into map because {}", data, ex.getMessage(), ex);
        }
        return ret;
    }

    public String get(String key) {
        String ret = null;
        try {
            //Get the hash value of the key
            int hash = getHash(key);
            //Get all Map s that are larger than the Hash value
            SortedMap<Integer, String> subMap = sortedMap.tailMap(hash);
            if (subMap.isEmpty()) {
                //If there is no one larger than the hash value of the key, start with the first node
                Integer i = sortedMap.firstKey();
                String serverNode = sortedMap.get(i);
                ret = serverNode.split("&&")[0];
                logger.debug("If | {} fall in {} server, virtual server is {}", key, serverNode, ret);
            } else {
                //The first Key is the nearest node clockwise past the node.
                Integer i = subMap.firstKey();
                String serverNode = subMap.get(i);
                ret = serverNode.split("&&")[0];
                logger.debug("Else | {} fall in {} server, virtual server is {}", key, serverNode, ret);
            }
        } catch (Exception ex) {
            logger.error("Caught exception while retrieving {} from map because {}", key, ex.getMessage(), ex);
        }
        return ret;
    }

    public String remove(String data) {
        String ret = null;
        try {
            Iterator<Entry<Integer, String>> iterator = sortedMap.entrySet().iterator();
            while (iterator.hasNext())  {
                Map.Entry<Integer, String> entry = iterator.next();

                String vn = entry.getValue();
                if(vn.contains(data)) {
                    logger.debug("Removing values {}", vn);
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            logger.error("Caught exception while removing entry {} from map", data);
        }
        return ret;
    }

    // Using FNV1_32_HASH algorithm to calculate the Hash value of the server,
    private int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }
}
