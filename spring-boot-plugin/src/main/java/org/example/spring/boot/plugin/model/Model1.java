package org.example.spring.boot.plugin.model;

public class Model1 extends BaseModel{
    public Model1(String name, String id) {
        super(name, id);
    }

    public String getTypeName(){
        return Model1.class.getTypeName();
    }
}
