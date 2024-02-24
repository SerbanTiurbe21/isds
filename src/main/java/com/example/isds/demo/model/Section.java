package com.example.isds.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class Section {
    private String title;
    private List<InterviewerFeedback> interviewers;
}
