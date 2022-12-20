package com.project.study.dao;

import com.project.study.model.Output;
import com.project.study.repository.OutputRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OutputDao {

    private final OutputRepository outputRepository;

    public Output getOutput(Long id){
        Optional<Output> outputOp = outputRepository.findById(id);
        if (outputOp.isPresent()){
            return outputOp.get();
        } else {
            throw new NullPointerException("output 정보가 없습니다. output id : " + id);
        }
    }


}
