package com.springbatchpractice.processor;

import com.springbatchpractice.model.StudentJdbc;
import com.springbatchpractice.model.StudentJson;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class StudentProcessor implements ItemProcessor<StudentJdbc, StudentJson> {

    @Override
    public @Nullable StudentJson process(StudentJdbc item) {
        System.out.println("Inside Student Processor");

        StudentJson student = new StudentJson();

        student.setId(item.getId());
        student.setFirstName(item.getFirstName());
        student.setLastName(item.getLastName());
        student.setEmail(item.getEmail());

        return student;
    }
}
