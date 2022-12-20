package com.project.study.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_log_info")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id")
    private Server server;

    @Column(name = "log_code")
    private String logCode;

    @Column(name = "log_info")
    private String logInfo;

    @Column(name = "log_status")
    private String logStatus;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

}
