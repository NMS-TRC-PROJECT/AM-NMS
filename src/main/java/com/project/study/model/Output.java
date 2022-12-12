package com.project.study.model;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_output_preset")
public class Output {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "output_id")
    private Long outputId;

    @Column(name = "output_folder")
    private String outputFolder;

    @Column(name = "container")
    private String container;

    @Column(name = "output_type")
    private String outputType;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @OneToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @OneToOne
    @JoinColumn(name = "audio_id")
    private Audio audio;
}
