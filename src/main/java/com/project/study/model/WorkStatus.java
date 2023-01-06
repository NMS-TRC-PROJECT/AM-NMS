package com.project.study.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "tb_work_status")
public class WorkStatus {

    @Id
    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    @Column(name = "frames")
    private int frames;

    @Column(name = "status")
    private int status;

    @Column(name = "error_string")
    private String errorString;

    @Column(name = "speed")
    private String speed;

    @Column(name = "percentage")
    private String percentage;

    @Column(name = "output_filename")
    private String outputFilename;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "service_type")
    private String serviceType;

    @OneToOne
    @JoinColumn(name = "input_id")
    private Input input;

    @OneToOne
    @JoinColumn(name = "output_id")
    private Output output;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)  // 검색 시 충돌 방지
    @JoinColumn(name = "server_id")
    private Server server;
}
