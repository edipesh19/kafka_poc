package org.example.spring.boot.plugin.registry;

import org.example.spring.boot.plugin.logic.BaseLogic;
import org.example.spring.boot.plugin.logic.Logic1;
import org.example.spring.boot.plugin.model.Model1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Logic1Registry implements AbstractLogicRegistry {
    private Logic1 logic1;

    @Autowired
    public Logic1Registry(Logic1 logic1) {
        this.logic1 = logic1;
    }

    @Override
    public String[] getSupportedModelType() {
        return new String[]{Model1.class.getTypeName()};
    }

    @Override
    public BaseLogic returnLogic() {
        return logic1;
    }
}
