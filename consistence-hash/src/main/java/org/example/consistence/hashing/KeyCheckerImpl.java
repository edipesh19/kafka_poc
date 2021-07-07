package org.example.consistence.hashing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class KeyCheckerImpl implements KeyChecker {
    private static Logger logger = LoggerFactory.getLogger(KeyCheckerImpl.class);
    private String hostIp;
    private KeyManager keyManager;

    @Autowired
    public KeyCheckerImpl(KeyManager keyManager) {
        this.keyManager = keyManager;
        try {
            hostIp = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            logger.error("Cannot get ip address of the host please set the ip address manually");
        }
    }

    @Override
    public boolean check(String key) {
        String host =keyManager.get(key);
        if(host != null && !host.isEmpty() && host.equals(hostIp)){
            return true;
        }
        return false;
    }

    @Override
    public void setHostName(String host) {
        if(host == null || host.isEmpty())
            throw new IllegalArgumentException("Host name cannot be null");
        this.hostIp = hostIp;
    }
}
