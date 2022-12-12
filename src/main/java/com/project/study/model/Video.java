package com.project.study.model;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_output_video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private Long videoId;

    @Column(name = "codec")
    private String videoCodec;

    @Column(name = "quality")
    private String quality;
    // MyBatis에서는 decimal방식이었는데, JPA에선 String으로 받아서 넘기는 방식으로 할게요~

    @Column(name = "bitrate")
    private int videoBitrate;

    @Column(name = "frame_rate")
    private String frameRate;

    @Column(name = "width")
    private int width;

    @Column(name = "height")
    private int height;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;


}
