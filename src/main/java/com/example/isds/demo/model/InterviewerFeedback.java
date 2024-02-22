package com.example.isds.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InterviewerFeedback {
    private String name;
    private String role;
    private String feedback;
    private Double score;
}
