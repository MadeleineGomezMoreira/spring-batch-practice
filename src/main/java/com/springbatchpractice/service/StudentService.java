package com.springbatchpractice.service;

import com.springbatchpractice.model.StudentJson;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class StudentService {

    List<StudentJson> list;

    public List<StudentJson> restCallToStudents() {
        RestTemplate restTemplate = new RestTemplate();
        StudentJson[] studentsArray =
                restTemplate.getForObject("http://localhost:8081/api/v1/students", StudentJson[].class);

        list = new ArrayList<>(Arrays.asList(studentsArray));

        return list;
    }

    public StudentJson getStudent(Long id, String name){
        System.out.println("ID: " + id + " NAME: " + name);
        if(list == null){
            restCallToStudents();
        }

        if(list !=null && !list.isEmpty()){
            return list.remove(0);
        }

        return null;
    }
}
