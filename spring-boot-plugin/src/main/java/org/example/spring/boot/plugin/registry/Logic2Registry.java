package org.example.spring.boot.plugin.registry;

import org.example.spring.boot.plugin.logic.BaseLogic;
import org.example.spring.boot.plugin.logic.Logic2;
import org.example.spring.boot.plugin.model.Model2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Logic2Registry implements AbstractLogicRegistry {
    private Logic2 logic2;

    @Autowired
    public Logic2Registry(Logic2 logic2) {
        this.logic2 = logic2;
    }

    @Override
    public String[] getSupportedModelType() {
        return new String[]{Model2.class.getTypeName()};
    }

    @Override
    public BaseLogic returnLogic() {
        return logic2;
    }
}
