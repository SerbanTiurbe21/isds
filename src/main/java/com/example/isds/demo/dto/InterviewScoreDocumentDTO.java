package com.example.isds.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InterviewScoreDocumentDTO {
    private String candidate;
    private String interviewDate;
    private String lastUpdateDate;
    private List<SectionDTO> sections;
    private double finalScore;
    private String appliedForRole;
    private String candidateId;
}