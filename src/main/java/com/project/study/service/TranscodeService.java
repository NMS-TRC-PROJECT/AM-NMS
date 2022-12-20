package com.project.study.service;

import com.project.study.common.util.APIUrl;
import com.project.study.common.util.HttpUtils;
import com.project.study.dao.InputDao;
import com.project.study.dao.OutputDao;
import com.project.study.dto.TranscodeReqDto;
import com.project.study.model.Input;
import com.project.study.model.Output;
import com.project.study.model.Server;
import com.project.study.model.WorkStatus;
import com.project.study.repository.InputRepository;
import com.project.study.repository.OutputRepository;
import com.project.study.repository.ServerRepository;
import com.project.study.repository.WorkStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TranscodeService {
    private final WorkStatusRepository workStatusRepository;
    private final ServerRepository serverRepository;
    private final InputRepository inputRepository;
    private final OutputRepository outputRepository;
    private final InputDao inputDao;
    private final OutputDao outputDao;


    // TRC VOD 서버 조회
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
        TranscodeReqDto dto = new TranscodeReqDto();

        // transaction id 만들기
        UUID uuid = UUID.randomUUID();
        String transactionId = uuid.toString();
        log.info("transactionId : " + transactionId);

        try {
            // 할당 가능한 TRC 서버 조회
            Server availableSvr = checkAvailableSvr();

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
            workStatus.setCreateDate(LocalDateTime.now());
            workStatus.setOutputFileName(outputOp.getOutputFileName());
            workStatus.setStatus(1);
            workStatus.setUpdateDate(LocalDateTime.now()); // 이게 맞아..? 수정하면 어쩔래
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

        Map<String, Object> presetDto = null;
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
}
