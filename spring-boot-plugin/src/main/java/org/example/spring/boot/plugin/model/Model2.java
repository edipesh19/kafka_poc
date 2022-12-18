package org.example.spring.boot.plugin.model;

public class Model2 extends BaseModel{
    public Model2(String name, String id) {
        super(name, id);
    }

    public String getTypeName(){
        return Model2.class.getTypeName();
    }
}
