package com.project.study.service;

import com.project.study.dto.WorkStatusRespDto;
import com.project.study.model.WorkStatus;
import com.project.study.repository.WorkStatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Rollback
class TranscodeServiceTest {

    @Autowired
    TranscodeService transcodeService;

    @Autowired
    WorkStatusRepository workStatusRepository;

    @Test
    @DisplayName("vod 작업요청 테스트")
    void requestTrcVod() {
        // given

        // when
        transcodeService.requestTrcVod();

        // then

    }

    @Test
    @DisplayName("vod 작업상태 업데이트 테스트")
    void updateStatus(){
        // given
        WorkStatus workStatus = workStatusRepository.findByTransactionId("0bdd0854-8c61-4db5-89f7-c66bc3b55f58");
        workStatus.setStatus(2);

        // when
        transcodeService.updateVodTrcStatus(workStatus);  //테스트 후에 DTO를 도입함 ㅋㅅㅋ 나는야 몽총잉

        // then

    }
}