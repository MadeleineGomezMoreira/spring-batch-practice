package com.springbatchpractice.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlRootElement(name = "student")
@Data
public class StudentJdbc {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    @Override
    public String toString() {
        return "-- STUDENT --" + '\n' +
                "ID: " + id + '\n' +
                "FIRST NAME: " + firstName + '\n' +
                "LAST NAME: " + lastName + '\n' +
                "EMAIL: " + email + '\n' +
                "------------";
    }
}
