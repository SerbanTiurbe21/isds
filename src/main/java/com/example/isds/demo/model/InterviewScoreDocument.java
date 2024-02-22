package com.example.isds.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "InterviewScore")
@Data
public class InterviewScoreDocument {
    @Id
    private String id;
    private List<Section> sections;
    private String candidateIdentifier;
    private LocalDate interviewDate;
    private LocalDate lastUpdate;
    private double finalScore;
    private String roleAppliedFor;
    private String candidateId;
}
