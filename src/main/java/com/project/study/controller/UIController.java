package com.project.study.controller;

import com.project.study.dto.WorkStatusRespDto;
import com.project.study.model.Input;
import com.project.study.model.Output;
import com.project.study.model.Server;
import com.project.study.model.WorkStatus;
import com.project.study.service.ServerService;
import com.project.study.service.TranscodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nms")
@RequiredArgsConstructor
@Slf4j
public class UIController {

    private final TranscodeService transcodeService;
    private final ServerService serverService;

    /* 대시보드용 */
    @GetMapping(value = "/dash/job/list", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> getDashBoardServerList(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> result = new HashMap<>();
        try{
            List<WorkStatusRespDto> jobList = transcodeService.findWorkStatusByStatus();
            result.put("jobList", jobList);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.info("대시보드 - job list 못 가지고 옴");
            e.printStackTrace();
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Server list
    @GetMapping(value = "/server/list", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> getServerList(@RequestParam(value = "page", defaultValue = "0") int page,
                                    HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<Server> serverList = serverService.findServerPaging(page);
            result.put("serverList", serverList);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.info("server list 넘어가기 실패");
            e.printStackTrace();
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get WorkStatus list
    @GetMapping(value = "/job/list", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> getJobList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try{
            List<WorkStatus> jobList = transcodeService.findWorkStatus();
            result.put("jobList", jobList);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.info("job list 못 가지고 옴");
            e.printStackTrace();
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping(value = "/preset/list", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> getPresetList(HttpServletResponse response, HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        List<Input> inputList = transcodeService.getInputPresetList();
        resultJson.put("inputList", inputList);
        List<Output> outputList = transcodeService.getOutputPresetList();
        resultJson.put("outputList", outputList);

        return new ResponseEntity<>(resultJson.toString(), HttpStatus.OK);
    }


}
