package com.project.study.dao;

import com.project.study.model.Input;
import com.project.study.repository.InputRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InputDao {
    private final InputRepository inputRepository;

    public Input getInput(Long id){
        Optional<Input> inputOP = inputRepository.findById(id);
        if(inputOP.isPresent()){
            return inputOP.get();
        } else {
            throw new NullPointerException("input 정보가 없습니다. input id : " + id);
        }
    }

    public List<Input> getAllInput(){
        List<Input> inputList = inputRepository.findAll();
        if(inputList.isEmpty()){
            throw new NullPointerException("input 정보가 없습니다.");
        } else {
            return inputList;
        }
    }
}
