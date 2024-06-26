package com.example.isds.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Section {
    private String title;
    private List<InterviewerFeedback> interviewers;
}
