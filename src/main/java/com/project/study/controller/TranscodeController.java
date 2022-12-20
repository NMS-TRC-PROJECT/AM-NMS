package com.project.study.controller;

import com.project.study.dto.TranscodeReqDto;
import com.project.study.service.TranscodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.json.simple.JSONObject;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TranscodeController {

    private final TranscodeService transcodeService;


    @PostMapping(value = "/transcoder/vod", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> requestTransVOD(HttpServletRequest request, HttpServletResponse response){
        // 컴파일러 Warning 을 피하기 위해 HashMap 이용
        // WARN : Unchecked call to 'put(K, V)' as a member of raw type 'java.util.HashMap'
        // JSONObject는 HashMap 클래스를 상속했지만, 그 자체는 Generic이 아니기 때문에 Key, Value를 명시할 수 없다.
        // 따라서 HashMap으로 K-V를 연결지어준 다음 JSONObject에 넣어주는 방식으로 코드짜기!

        JSONObject resultJson = null;

        try {
            // 냅다 VOD TRC 요청
            resultJson = transcodeService.requestTrcVod();

        } catch (Exception e) {
            e.printStackTrace();
            log.info("requestTransVOD - 요청 실패");
        }
        assert resultJson != null;
        log.info("resultJson.toString() : " + resultJson.toString());

        return new ResponseEntity<>(resultJson.toString(), HttpStatus.OK);
    }
}
