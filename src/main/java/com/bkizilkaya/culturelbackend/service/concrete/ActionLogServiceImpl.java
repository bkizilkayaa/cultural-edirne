package com.bkizilkaya.culturelbackend.service.concrete;

import com.bkizilkaya.culturelbackend.dto.actionlog.ActionLogDTO;
import com.bkizilkaya.culturelbackend.dto.spot.response.TouristSpotResponseDTO;
import com.bkizilkaya.culturelbackend.mapper.ActionLogMapper;
import com.bkizilkaya.culturelbackend.mapper.TouristSpotMapper;
import com.bkizilkaya.culturelbackend.model.ActionEnum;
import com.bkizilkaya.culturelbackend.model.ActionLog;
import com.bkizilkaya.culturelbackend.model.TableNameEnum;
import com.bkizilkaya.culturelbackend.repo.ActionLogRepository;
import com.bkizilkaya.culturelbackend.service.abstraction.ActionLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ActionLogServiceImpl implements ActionLogService {
    private final ActionLogRepository actionLogRepository;

    public ActionLogServiceImpl(ActionLogRepository actionLogRepository) {
        this.actionLogRepository = actionLogRepository;
    }

    @Override
    public void createLog(ActionEnum actionEnum, TableNameEnum tableName, String jsonObject) {
        ActionLog action = actionLogBuilder(actionEnum, tableName.getTableName(), jsonObject);
        actionLogRepository.save(action);
    }

    public Page<ActionLogDTO> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.actionLogRepository.findAll(pageable).map(ActionLogMapper.INSTANCE::entityToDto);
    }

    protected ActionLog actionLogBuilder(ActionEnum actionEnum, String tableName, String jsonObject){
        return ActionLog.builder().action(actionEnum.getAction())
                .tableName(tableName)
                .logDetail(jsonObject)
                //.user() bu satırda da hangi user olduğunu geçicem.
                .build();
    }
}
