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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

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

    // NMS -> TRC
    // 작업 요청
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
            String serviceType = "ffmpegTRC_2";

            Input inputOp = inputDao.getInput(inputID);
            Output outputOp = outputDao.getOutput(outputID);

            dto = this.getTranscodeReqDto(inputOp, outputOp);
            dto.setTransactionId(transactionId);
            dto.setServiceType(serviceType);

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
            workStatus.setServiceType(serviceType);

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

            // Object가 아니라 array로 바꾼다면?!
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
    public JSONObject updateVodTrcStatus(WorkStatusRespDto workStatus) {
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

        Exception ex = null;

        if (workStatus2 == null) {
            log.info("잘못된 transactionId로 작업 상태 업데이트 실패");
            ex = new Exception();

            resultJson.put("resultCode", 404);
            resultJson.put("errorString", String.format("transactionId %s 는 존재하지 않습니다.", workStatus.getTransactionId()));
        } else {
            workStatus2.setUpdateDate(now());

            try {
                workStatus2.setPercentage(workStatus.getPercentage());
                workStatus2.setSpeed(workStatus.getSpeed());
                workStatus2.setFrames(workStatus.getFrames());
                workStatus2.setErrorString(workStatus.getErrorString());
                workStatus2.setStatus(workStatus.getStatus());
                workStatus2.setOutputFilename(workStatus.getTranscodes().get(0).get("outputFilename").toString());

                workStatusRepository.save(workStatus2);
                log.info("작업 상태 업데이트 완료");
            } catch (Exception e) {
                e.printStackTrace();
                ex = e;
                log.info("service catched - 업데이트 실패");
            }
        }
        resultJson.put("resultCode", ex==null ? 200 : 500);
        resultJson.put("errorString", ex==null ? "" : ex.toString());

        return resultJson;
    }

    @Transactional
    public JSONObject cancelVODTranscoding(String transactionId) throws IOException {
        JSONObject resultJson = new JSONObject();
        WorkStatus workStatus = workStatusRepository.findByTransactionId(transactionId);

        if (workStatus != null && workStatus.getStatus() != 0){
            workStatus.setStatus(3);
            workStatus.setErrorString("취소된 작업");
            workStatus.setUpdateDate(now());

                // 3. Transcoder로 취소 지시
                log.info("transcoder로 취소 지시");
                log.info(workStatus.getTransactionId());
                String response = HttpUtils.sendRequest("http://"+workStatus.getServer().getServerIp()+":"+workStatus.getServer().getServerPort()+APIUrl.TR_CTRL_VOD_URL+"/"+transactionId, "DELETE");
                log.info("response : " + response);
                resultJson = new JSONObject(response);

                if (resultJson.getInt("resultCode") == 200 || resultJson.getInt("resultCode") == 201) {
                    resultJson.put("resultCode", "200");
                    workStatusRepository.save(workStatus);
                }

        } else {
            resultJson.put("resultCode", "404");
            log.info("해당 transactionId의 작업이 존재하지 않음");
        }
        return resultJson;
    }

    public List<WorkStatus> findWorkStatus() {
        return workStatusRepository.findAllByOrderByUpdateDateDesc();
    }

    // 대시보드용 진행중인 job만 가져오기
    public List<WorkStatusRespDto> findWorkStatusByStatus(){
        List<WorkStatusRespDto> dtoList = new ArrayList<>();
        List<WorkStatus> originList = workStatusRepository.findByStatusAndUpdateDateAfterOrderByUpdateDateDesc(2, now().minusDays(1));
        for (int i=0; i<originList.size(); i++) {
            WorkStatusRespDto dto = new WorkStatusRespDto();
            dto.setServerId(originList.get(i).getServer().getServerId());
            dto.setTransactionId(originList.get(i).getTransactionId());
            dto.setStatus(originList.get(i).getStatus());
            dto.setPercentage(originList.get(i).getPercentage());
            dto.setUpdateDate(originList.get(i).getUpdateDate());
            dtoList.add(i,dto);
        }

        return dtoList;
    }

    public List<Input> getInputPresetList(){
        return inputDao.getAllInput();
    }

    public List<Output> getOutputPresetList() {
        return outputDao.getAllOutput();
    }
}
