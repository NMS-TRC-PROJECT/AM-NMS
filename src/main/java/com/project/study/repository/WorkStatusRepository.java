package com.project.study.repository;

import com.project.study.model.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkStatusRepository extends JpaRepository<WorkStatus, Long> {

    WorkStatus findByTransactionId(String transactionId);
}
