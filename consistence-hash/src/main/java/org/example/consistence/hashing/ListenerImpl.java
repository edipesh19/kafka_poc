package org.example.consistence.hashing;

import org.example.consistence.hashing.list.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListenerImpl implements Listener {
    Logger logger = LoggerFactory.getLogger(ListenerImpl.class);

    private HostManager consistenceHashing;

    @Autowired
    public ListenerImpl(HostManager consistenceHashing) {
        this.consistenceHashing = consistenceHashing;
    }

    @Override
    public boolean put(String str) {
        return consistenceHashing.put(str) != null;
    }

    @Override
    public boolean remove(String str) {
        return consistenceHashing.remove(str) !=null;
    }
}
