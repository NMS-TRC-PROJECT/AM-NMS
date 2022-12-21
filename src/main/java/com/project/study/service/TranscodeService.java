package com.project.study.service;

import com.project.study.common.util.APIUrl;
import com.project.study.common.util.HttpUtils;
import com.project.study.dao.InputDao;
import com.project.study.dao.OutputDao;
import com.project.study.dto.TranscodeReqDto;
import com.project.study.dto.WorkStatusRespDto;
import com.project.study.model.Input;
import com.project.study.model.Output;
import com.project.study.model.Server;
import com.project.study.model.WorkStatus;
import com.project.study.repository.ServerRepository;
import com.project.study.repository.WorkStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Service
@Slf4j
@RequiredArgsConstructor
public class TranscodeService {

    private final ServerService serverService;  // 서버 관련된 메소드를 묶으려고 이렇게 참조하긴 했는데.. 과연 이게 괜찮을까..?
    private final WorkStatusRepository workStatusRepository;
    private final ServerRepository serverRepository;
    private final InputDao inputDao;
    private final OutputDao outputDao;


    // TRC VOD 서버 조회            -> serverservice로 옮김
    private Server checkAvailableSvr(){
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

    // NMS -> TRC
    // 작업 요청
    // 회원가입 페이지(나) -post-> 회원가입 로직(상현)
    @Transactional
    public JSONObject requestTrcVod() {
        JSONObject result = new JSONObject();
        WorkStatus workStatus = new WorkStatus();
        TranscodeReqDto dto;

        // transaction id 만들기
        UUID uuid = UUID.randomUUID();
        String transactionId = uuid.toString();
        log.info("transactionId : " + transactionId);

        try {
            // 할당 가능한 TRC 서버 조회
            Server availableSvr = serverService.checkAvailableSvr();

            // 1. dto에 input, output, video, audio 정보를 담기
            // input, output id = 일단 1L
            long inputID = 1L;
            long outputID = 1L;

            Input inputOp = inputDao.getInput(inputID);
            Output outputOp = outputDao.getOutput(outputID);

            dto = this.getTranscodeReqDto(inputOp, outputOp);
            dto.setTransactionId(transactionId);

            // 2. 작업상태 테이블(tb_work_status)에 저장
            // transactionId, create_date, output_filename, status, update_date, input_id, output_id, server_id
            workStatus.setTransactionId(transactionId);
            workStatus.setCreateDate(now());
            workStatus.setOutputFilename(outputOp.getOutputFileName());
            workStatus.setStatus(1);
            workStatus.setUpdateDate(now()); // 이게 맞아..? 수정하면 어쩔래
            workStatus.setInput(inputOp);
            workStatus.setOutput(outputOp);
            workStatus.setServer(availableSvr);

            workStatusRepository.save(workStatus);
            log.info("작업 상태 저장 완료");

            try {
                // 3. Transcoder로 작업 지시
                log.info("JSONObject : " + new JSONObject(dto).toString());
                String response = HttpUtils.sendPost("http://"+availableSvr.getServerIp()+":"+availableSvr.getServerPort()+APIUrl.TR_CTRL_VOD_URL, new JSONObject(dto));
                log.info("response : " + response);
                result = new JSONObject(response);
                log.info("transcoder로 작업 지시");

            } catch (Exception e ){
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();

        }
        return result;
    }

    // VOD 작업 요청 DTO 만들기 용
    public TranscodeReqDto getTranscodeReqDto(Input input, Output output){
        TranscodeReqDto dto = new TranscodeReqDto();
        log.info("getTranscodeReqDto 시작");

        Map<String, Object> presetDto;
        try {
            // basic -> object 타입으로 선언
            presetDto = new HashMap<>();
            presetDto.put("inputFilename", input.getInputFileName());
            presetDto.put("outputFilename", output.getOutputFileName());
            presetDto.put("inputFolder", input.getInputFolder());
            presetDto.put("outputFolder", output.getOutputFolder());

            dto.setBasic(presetDto);

            // outputs -> Object 타입으로 선언
            presetDto = new HashMap<>();
            presetDto.put("container", output.getContainer());
            presetDto.put("outputType", output.getOutputType());

            // video
            Map<String, Object> presetMap = new HashMap<>();
            presetMap.put("codec", output.getVideo().getVideoCodec());
            presetMap.put("quality", output.getVideo().getQuality());
            presetMap.put("bitrate", output.getVideo().getVideoBitrate());
            presetMap.put("framerate", output.getVideo().getFrameRate());
            presetMap.put("resolutionWidth", output.getVideo().getWidth());
            presetMap.put("resolutionHeight", output.getVideo().getHeight());
            presetDto.put("video", presetMap);

            // audio
            presetMap = new HashMap<>();
            presetMap.put("codec", output.getAudio().getAudioCodec());
            presetMap.put("bitrate", output.getAudio().getAudioBitrate());
            presetDto.put("audio", presetMap);

            dto.setOutputs(presetDto);
        } catch (Exception e){
            e.printStackTrace();
        }
        return dto;
    }

    @Transactional
    public JSONObject updateVodTrcStatus(/*String transactionId, */ WorkStatusRespDto workStatus) {
//        status = 0 -> 완료
//                percentage : 100
//        status = 1 -> 대기
//        status = 2 -> 진행중
//                percentage, speed, frames 업데이트
//        status = 3 -> 취소
//        status = -1 -> 에러
//                errorString, percentage, speed, frames 업데이트

        // 일단 해당 transactionId로 Workstatus찾아야해!
        WorkStatus workStatus2 = workStatusRepository.findByTransactionId(workStatus.getTransactionId());
        JSONObject resultJson = new JSONObject();

        workStatus2.setUpdateDate(now());
        try {
            if (workStatus.getStatus() == 0) {
                workStatus2.setPercentage("100");
            } else {
                workStatus2.setPercentage(workStatus.getPercentage());
                workStatus2.setSpeed(workStatus.getSpeed());
                workStatus2.setFrames(workStatus.getFrames());
                workStatus2.setErrorString(workStatus.getErrorString());
                workStatus2.setStatus(workStatus.getStatus());
                workStatus2.setOutputFilename(workStatus.getTranscodes().get(0).get("outputFilename").toString());
            }
            workStatusRepository.save(workStatus2);
            log.info("작업 상태 업데이트 완료");
            resultJson.put("resultCode", 200);
            resultJson.put("errorString", "");  // 이거 해야하는 건가여!??!!!!?!?!?!
        } catch (Exception e) {
            e.printStackTrace();
            resultJson.put("resultCode", 500);
            resultJson.put("errorString", e.toString());
            log.info("service catched - 업데이트 실패");
        }

        return resultJson;
    }
}