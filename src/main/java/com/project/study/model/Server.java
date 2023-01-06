package com.project.study.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "tb_svr_mst")
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "server_id")
    private Long serverId;

    @Column(name = "server_name")
    private String serverName;

    @Column(name = "server_port")
    private int serverPort;

    @Column(name = "server_ip")
    private String serverIp;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    // 연관 관계 매핑
    @JsonIgnore
    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL)
    private List<Log> log = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "server")
    private List<WorkStatus> workStatuses = new ArrayList<>();

    @Override
    public String toString() {
        return "Server{" +
                "serverId=" + serverId +
                ", serverName='" + serverName + '\'' +
                ", serverPort=" + serverPort +
                ", serverIp='" + serverIp + '\'' +
                ", isActive='" + isActive + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
