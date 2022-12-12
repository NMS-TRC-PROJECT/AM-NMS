package com.project.study.controller;

import com.project.study.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ServerController {

    private final ServerService serverService;

    // 서버 상태 체크
    @Scheduled(cron = "*/10 * * * * *")
    public void checkServer() throws Exception{
        Long serverId = 1L;
        try{
            serverService.checkServerStatus(serverId);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
