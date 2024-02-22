package com.example.isds.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SectionDTO {
    private String title;
    private List<InterviewerFeedbackDTO> interviewers;
}
