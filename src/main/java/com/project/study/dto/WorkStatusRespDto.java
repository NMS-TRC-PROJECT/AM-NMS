package com.project.study.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class WorkStatusRespDto {

    private long serverId;
    private String transactionId;
    private int frames;
    private int status;

    private String errorString;
    private String speed;

    private String percentage;
    private LocalDateTime updateDate;
    private List<Map<String, Object>> transcodes;

}
