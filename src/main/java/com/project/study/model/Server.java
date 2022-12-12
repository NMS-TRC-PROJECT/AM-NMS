package com.project.study.model;

//import com.project.study.dto.ServerReqDto;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

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

//    // 서버 내용 전체용 (insert 할 때 주로 사용)
//    @Builder(builderClassName = "serverAllBuilder", builderMethodName = "serverAllBuilder")
//    public Server(ServerReqDto serverReqDto){
////        Assert.hasText(String.valueOf(serverId), "서버아이디는 [null]이 될 수 없습니다.");
////        Assert.hasText(serverName, "서버이름은 [null]이 될 수 없습니다.");
////        Assert.hasText(String.valueOf(serverPort), "서버포트는 [null]이 될 수 없습니다.");
////        Assert.hasText(serverIp, "서버IP는 [null]이 될 수 없습니다.");
////        Assert.hasText(isActive, "서버활성화여부는 [null]이 될 수 없습니다.");
//
//        this.serverId = serverReqDto.getServerId();
//        this.serverName = serverName;
//        this.serverPort = serverPort;
//        this.serverIp = serverIp;
//        this.isActive = isActive;
//    }
//
//    // 서버 상태 업데이트용 (update 할 때 주로 사용)
//    @Builder(builderClassName = "serverUpdateBuilder", builderMethodName = "serverUpdateBuilder")
//    public Server(String isActive){
//        Assert.hasText(String.valueOf(isActive), "서버활성화여부는 [null]이 될 수 없습니다.");
//        this.isActive = isActive;
//    }

    // 연관 관계 매핑
    @OneToOne(mappedBy = "server", cascade = CascadeType.ALL)
    private Log log;

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
