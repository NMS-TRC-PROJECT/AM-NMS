package com.project.study.service;

import com.project.study.common.util.APIUrl;
import com.project.study.common.util.HttpUtils;
import com.project.study.exception.ServerNotFoundException;
import com.project.study.model.Server;
import com.project.study.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServerService {

    private final ServerRepository serverRepository;

    // 서버 상태 체크 (MT003)
    @Async
    @Transactional(rollbackOn = RuntimeException.class) // 런타임 예외 발생 시 롤백
    public void checkServerStatus(Long serverId) {
        Server serverVo = serverRepository.findById(serverId).orElseThrow(() -> new ServerNotFoundException(serverId));
        String sendUrl;

        try {
            // 서버 상태 냅다 받기 (Id, Ip, port는 항상 동일하니까 이렇게 줘도 됨!)
            sendUrl = "http://" + serverVo.getServerIp()+ ":"+serverVo.getServerPort()+ APIUrl.TR_SERVER_STATUS_URL;
            String response = HttpUtils.sendGet(sendUrl, 5000, 5000);

            // set server status
            serverVo.setIsActive("1");
            log.info("서버 정상");

        } catch (Exception e){
            // set server status
            serverVo.setIsActive("0");
            log.info("서버 끊김");
        }
        serverVo.setUpdateDate(LocalDateTime.now());

        // update server status
        try{
            serverRepository.save(serverVo);
            log.info("서버 업데이트 완료");
        } catch (Exception e){
            log.info("서버 업데이트 실패");
            e.printStackTrace();
        }
    }

    // TRC VOD 서버 조회
    public Server checkAvailableSvr(){
        Server serverInfo = null;

        try{
            serverInfo = serverRepository.findTopByIsActiveOrderByWorkStatusesAsc("1");
            log.info("사용 가능한 서버 찾았음 : " + serverInfo.getServerIp());
        } catch (Exception e){
            e.printStackTrace();
            log.info("사용 가능한 서버 못 찾음");
        }

        return serverInfo;
    }

    // Server Id로 Server 객체 가져오기
    public Server getServer(Long serverId) {
        
        return serverRepository.findById(serverId).orElseThrow(() -> new ServerNotFoundException(serverId));
    }

    // ServerList + Paging
    public Page<Server> findServerPaging(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        log.info("server list 가지고 넘어가요");
        return serverRepository.findAll(pageable);
    }
}
