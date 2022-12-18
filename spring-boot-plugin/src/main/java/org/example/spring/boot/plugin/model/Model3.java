package org.example.spring.boot.plugin.model;

public class Model3 extends BaseModel{
    public Model3(String name, String id) {
        super(name, id);
    }

    public String getTypeName(){
        return Model3.class.getTypeName();
    }
}
