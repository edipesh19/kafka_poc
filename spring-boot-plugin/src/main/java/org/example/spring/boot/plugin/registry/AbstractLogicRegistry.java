package org.example.spring.boot.plugin.registry;

import org.example.spring.boot.plugin.logic.BaseLogic;
import org.example.spring.boot.plugin.model.BaseModel;
import org.springframework.stereotype.Component;

public interface AbstractLogicRegistry extends BaseLogicRegistry<BaseModel>{
    public BaseLogic returnLogic();
    default BaseLogic getLogicFor(BaseModel model) {
        if(model instanceof BaseModel) {
            return returnLogic();
        }
        return null;
    }
}
