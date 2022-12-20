package com.project.study.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@Rollback
class TranscodeServiceTest {

    @Autowired
    TranscodeService transcodeService;

    @Test
    @DisplayName("vod 작업요청 테스트")
    void requestTrcVod() {
        // given

        // when
        transcodeService.requestTrcVod();

        // then

    }
}