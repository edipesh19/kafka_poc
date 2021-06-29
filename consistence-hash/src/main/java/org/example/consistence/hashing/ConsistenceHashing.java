package org.example.consistence.hashing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.SortedMap;
import java.util.TreeMap;

// Todo : Implement virtual node
// Todo : Implement test for scale-out and scale-in scenarios.
public class ConsistenceHashing {

    Logger logger = LoggerFactory.getLogger(ConsistenceHashing.class);

    //key represents the hash value of the server and value represents the server
    private static SortedMap<Integer, String> sortedMap = new TreeMap<Integer, String>();

    public String put(String data) {
        String ret = null;
        try {
            int hash = getHash(data);
            ret = sortedMap.put(hash, data);
            logger.info("Server [{}] added to the map", data);
        } catch(Exception ex) {
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
            if(subMap.isEmpty()){
                //If there is no one larger than the hash value of the key, start with the first node
                Integer i = sortedMap.firstKey();
                ret =  sortedMap.get(i);
            }else{
                //The first Key is the nearest node clockwise past the node.
                Integer i = subMap.firstKey();
                ret =  subMap.get(i);
            }
        } catch (Exception ex) {
            logger.error("Caught exception while retrieving {} from map because {}", key, ex.getMessage(), ex);
        }
        return ret;
    }

    //Using FNV1_32_HASH algorithm to calculate the Hash value of the server,
    // there is no need to rewrite hashCode method, the final effect is no difference.
    private int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // If the calculated value is negative, take its absolute value.
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }
}
