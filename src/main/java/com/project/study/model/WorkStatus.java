package com.project.study.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_work_status")
public class WorkStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

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
    private String outputFileName;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToOne
    @JoinColumn(name = "input_id")
    private Input input;

    @OneToOne
    @JoinColumn(name = "output_id")
    private Output output;
}
