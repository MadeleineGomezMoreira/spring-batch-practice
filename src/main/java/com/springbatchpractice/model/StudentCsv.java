package com.springbatchpractice.model;

import lombok.Data;

@Data
public class StudentCsv {

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
