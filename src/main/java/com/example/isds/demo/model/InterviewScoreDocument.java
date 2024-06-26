package com.example.isds.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "InterviewScore")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewScoreDocument {
    @Id
    private String id;
    private List<Section> sections;
    private LocalDate interviewDate;
    private LocalDate lastUpdate;
    private double finalScore;
    private String roleAppliedFor;
    private String candidateId;
    private DocumentStatus status;
}
