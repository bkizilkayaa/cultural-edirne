package com.bkizilkaya.culturelbackend.repo;

import com.bkizilkaya.culturelbackend.model.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
}
