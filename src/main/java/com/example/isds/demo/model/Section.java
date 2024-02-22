package com.example.isds.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
public class Section {
    private String title;
    private List<InterviewerFeedback> interviewers;
}
