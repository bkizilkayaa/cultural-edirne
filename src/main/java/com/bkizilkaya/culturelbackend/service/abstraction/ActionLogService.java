package com.bkizilkaya.culturelbackend.service.abstraction;

import com.bkizilkaya.culturelbackend.model.ActionEnum;
import com.bkizilkaya.culturelbackend.model.TableNameEnum;

public interface ActionLogService {
    void createLog(ActionEnum actionEnum, TableNameEnum tableName, String jsonObject);
}
