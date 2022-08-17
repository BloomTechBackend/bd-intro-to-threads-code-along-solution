package com.bloomtech.codealong.MagicEightBall.model;

import java.util.ArrayList;
import java.util.List;
// Data sent with the HTTP request
// Object of class will be instantiated by Spring Boot when referenced
//        in an @RequestBody annotation in a method parameter

public class MagicEightBallRequest {

    // data store for array of questions sent with request
    //      JSON array attribute name in the request must match instance variable name
    private List<String> questions;

    // Defensive return a copy of the instance data
    public List<String> getQuestions() {
        return new ArrayList(questions);
    }
}
