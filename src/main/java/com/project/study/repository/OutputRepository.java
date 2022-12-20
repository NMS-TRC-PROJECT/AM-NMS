package com.project.study.repository;

import com.project.study.model.Output;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutputRepository extends JpaRepository<Output, Long> {

}
