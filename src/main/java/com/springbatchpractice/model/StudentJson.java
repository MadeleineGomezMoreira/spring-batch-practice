package com.springbatchpractice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
//This annotation gets rid of the properties that are not marked within this file but are in the JSON file
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentJson {

    private Long id;

    //We would need this annotation if the qualifiers did not match
    @JsonProperty("firstName")
    private String firstName;

    //We would need this annotation if we did not want this key to be acknowledged
    //@JsonIgnore
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
