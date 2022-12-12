package com.project.study.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_work_input")
public class Input {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "input_id")
    private Long inputId;

    @Column(name = "source_path")
    private String sourcePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "input_Folder")
    private String inputFolder;
}
