package com.project.study.repository;

import com.project.study.model.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface WorkStatusRepository extends JpaRepository<WorkStatus, Long> {

    WorkStatus findByTransactionId(String transactionId);

    List<WorkStatus> findAllByOrderByUpdateDateDesc();
    List<WorkStatus> findByStatusAndUpdateDateAfterOrderByUpdateDateDesc(int status, LocalDateTime ldt);
}
