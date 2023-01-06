package com.project.study.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranscodeReqDto {
    private String transactionId;
    private Object outputs;
    private Object basic;

    private String serviceType;


    public TranscodeReqDto(){}

}
