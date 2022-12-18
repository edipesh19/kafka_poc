package org.example.spring.boot.plugin.registry;

import org.example.spring.boot.plugin.model.BaseModel;
import org.springframework.plugin.core.Plugin;

public interface BaseLogicRegistry<T extends BaseModel> extends Plugin<T> {
    public String [] getSupportedModelType();

    @Override
    default boolean supports(T obj) {
        for (String supportedObjectType : getSupportedModelType()) {
            if (supportedObjectType != null && supportedObjectType.equals(obj.getTypeName())) {
                return true;
            }
        }
        return false;
    }

}
