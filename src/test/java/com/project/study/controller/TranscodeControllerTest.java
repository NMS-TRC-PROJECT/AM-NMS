//package com.project.study.controller;
//
//import com.project.study.service.TranscodeService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.annotation.Rollback;
//
//import javax.transaction.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//@Rollback
//class TranscodeControllerTest {
//
//    @Autowired
//    TranscodeController transcodeController;
//
//    @Test
//    @DisplayName("vod 작업 요청 테스트")
//    void requestTransVOD() {
//        // given
//
//        // when
//        ResponseEntity<?> response = transcodeController.requestTransVOD();
//
//        // then
//        assertEquals(jsonObject.getsta);
//    }
//}