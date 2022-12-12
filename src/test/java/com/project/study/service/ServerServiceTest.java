package com.project.study.service;

import com.project.study.model.Server;
import com.project.study.repository.ServerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class) // 가짜 메모리 환경 만들기
class ServerServiceTest {

    @InjectMocks
    private ServerService serverService;

    @Mock // 가짜 메모리 환경에 띄우기 (인터페이스 = 익명 클래스가 뜸)
    private ServerRepository serverRepository;

    @DisplayName("서버 상태 업데이트 테스트")
    @Test
    public void checkServerStatus() {
        // given
//        Server server = new Server();
//        server.setServerId(1L);
//        server.setIsActive("0");
        // when

        // then
//        assertThat
    }
}