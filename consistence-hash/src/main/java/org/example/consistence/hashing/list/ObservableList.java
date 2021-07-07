package org.example.consistence.hashing.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ObservableList extends ArrayList<String> {
    private Logger logger = LoggerFactory.getLogger(ObservableList.class);
    private Listener listener;

    @Autowired
    public ObservableList(Listener listener) {
        this.listener = listener;
    }

    public boolean add(String str){
        if(listener.put(str)) {
            return this.add(str);
        }
        return false;
    }

    public boolean remove(String str) {
        if(listener.remove(str)) {
            return this.remove(str);
        }
        return false;
    }
}
