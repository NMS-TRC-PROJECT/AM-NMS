package com.project.study.repository;

import com.project.study.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {
    Server findByServerId(Long serverId);

    //@Modifying
//    @Query(value = "update Server s set s.isActive = :isActive, s.updateDate = :updateDate  where s.serverId = :serverId")
//    void updateServerStatus(@Param("serverId") Long serverId, @Param("isActive") String isActive, @Param("updateDate")LocalDateTime updateDate);


}
