package com.example.isds.demo.dto;

import com.example.isds.demo.model.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InterviewScoreDocumentDTO {
    private String interviewDate;
    private String lastUpdateDate;
    private List<SectionDTO> sections;
    private double finalScore;
    private String appliedForRole;
    private String candidateId;
    private DocumentStatus status;
}