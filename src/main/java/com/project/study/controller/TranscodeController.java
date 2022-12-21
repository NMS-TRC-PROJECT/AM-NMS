package com.project.study.controller;

import com.project.study.dto.WorkStatusRespDto;
import com.project.study.service.ServerService;
import com.project.study.service.TranscodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TranscodeController {

    private final TranscodeService transcodeService;
    private final ServerService serverService;


    // NMS -> TRC
    // VOD 작업 상태 요청
    @PostMapping(value = "/transcoder/vod", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> requestTransVOD(HttpServletRequest request, HttpServletResponse response){
        // 컴파일러 Warning 을 피하기 위해 HashMap 이용
        // WARN : Unchecked call to 'put(K, V)' as a member of raw type 'java.util.HashMap'
        // JSONObject는 HashMap 클래스를 상속했지만, 그 자체는 Generic이 아니기 때문에 Key, Value를 명시할 수 없다.
        // 따라서 HashMap으로 K-V를 연결지어준 다음 JSONObject에 넣어주는 방식으로 코드짜기!

        JSONObject resultJson;

        try {
            // 냅다 VOD TRC 요청
            resultJson = transcodeService.requestTrcVod();

            log.info("resultJson.toString() : " + resultJson.toString());
            return new ResponseEntity<>(resultJson.toString(), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("requestTransVOD - 요청 실패");

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // NMS <- TRC
    // VOD 작업 상태 업데이트
    @PutMapping(value = "/trc/vod/status")
    public ResponseEntity<?> updateVodTrcStatus(@RequestBody WorkStatusRespDto workStatus, HttpServletRequest request, HttpServletResponse response){

        JSONObject resultJson;
        try{
            
            // 오류 잡기
            resultJson = transcodeService.updateVodTrcStatus(workStatus);

            return new ResponseEntity<>(resultJson.toString(), HttpStatus.OK);

        }  catch (Exception e){
            e.printStackTrace();
            log.info("updateVodTrcStatus - 작업 상태 업데이트 실패");

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}