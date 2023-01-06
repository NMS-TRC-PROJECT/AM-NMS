package com.project.study.repository;

import com.project.study.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {
    Server findTopByIsActiveOrderByWorkStatusesAsc(String isActive);

}
