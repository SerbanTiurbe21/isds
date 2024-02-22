package com.example.isds.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class InterviewerFeedbackDTO {
    String name;
    Double score;
    String feedback;
}