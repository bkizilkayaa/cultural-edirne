package com.bkizilkaya.culturelbackend.service.concrete;

import com.bkizilkaya.culturelbackend.model.ActionEnum;
import com.bkizilkaya.culturelbackend.model.ActionLog;
import com.bkizilkaya.culturelbackend.model.TableNameEnum;
import com.bkizilkaya.culturelbackend.repo.ActionLogRepository;
import com.bkizilkaya.culturelbackend.service.abstraction.ActionLogService;
import org.springframework.stereotype.Service;

@Service
public class ActionLogServiceImpl implements ActionLogService {
    private final ActionLogRepository actionLogRepository;

    public ActionLogServiceImpl(ActionLogRepository actionLogRepository) {
        this.actionLogRepository = actionLogRepository;
    }

    @Override
    public void createLog(ActionEnum actionEnum, TableNameEnum tableName) {
        ActionLog action = actionLogBuilder(actionEnum, tableName.getTableName());
        actionLogRepository.save(action);
    }

    protected ActionLog actionLogBuilder(ActionEnum actionEnum, String tableName){
        return ActionLog.builder().action(actionEnum.getAction())
                .tableName(tableName)
                //.user() bu satırda da hangi user olduğunu geçicem.
                .build();
    }
}
