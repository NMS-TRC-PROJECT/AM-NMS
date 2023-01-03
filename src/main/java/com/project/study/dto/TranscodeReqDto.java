package com.project.study.dto;

import com.project.study.model.Audio;
import com.project.study.model.Input;
import com.project.study.model.Output;
import com.project.study.model.Video;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.*;

@Getter
@Setter
public class TranscodeReqDto {
    private String transactionId;
    private Object outputs;
    private Object basic;

    private String serviceType;


    public TranscodeReqDto(){}

}
