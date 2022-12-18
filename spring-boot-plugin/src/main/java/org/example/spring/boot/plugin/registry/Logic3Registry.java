package org.example.spring.boot.plugin.registry;

import org.example.spring.boot.plugin.logic.BaseLogic;
import org.example.spring.boot.plugin.logic.Logic2;
import org.example.spring.boot.plugin.logic.Logic3;
import org.example.spring.boot.plugin.model.Model2;
import org.example.spring.boot.plugin.model.Model3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Logic3Registry implements AbstractLogicRegistry {
    private Logic3 logic3;

    @Autowired
    public Logic3Registry(Logic3 logic3) {
        this.logic3 = logic3;
    }

    @Override
    public String[] getSupportedModelType() {
        return new String[]{Model3.class.getTypeName()};
    }

    @Override
    public BaseLogic returnLogic() {
        return logic3;
    }
}
