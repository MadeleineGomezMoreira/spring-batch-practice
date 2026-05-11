package com.springbatchpractice.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "student")
public class StudentXml {

    private Long id;

    //Use this when there is a mismatch between name here and in the file
    @XmlElement(name = "firstName")
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
