package com.project.study.repository;

import com.project.study.model.Server;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServerRepositoryTest {

    @Autowired
    ServerRepository serverRepository;
    @Test
    void findByServerId() {
    }

    @DisplayName("[REPO]서버 상태 업데이트 테스트")
    @Test
    @Transactional
    void updateServerStatus() {

        // given
        Server server = serverRepository.findById(1L).orElse(null);

        if (server != null) {
            server.setUpdateDate(LocalDateTime.now());
            server.setIsActive("1");
        }

        // when
        assert server != null;
        serverRepository.save(server);

        //then
        assertEquals("1", server.getIsActive());
//        server.toString();
    }
}