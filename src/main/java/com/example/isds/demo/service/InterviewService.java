package com.example.isds.demo.service;

import com.example.isds.demo.dto.InterviewScoreDocumentDTO;
import com.example.isds.demo.model.InterviewScoreDocument;

import java.util.List;
import java.util.Optional;

public interface InterviewService {
    List<String> getValidSectionTitles();
    InterviewScoreDocument createInterviewScoreDocument(InterviewScoreDocument interviewScoreDocument);
    Optional<InterviewScoreDocument> getInterviewById(String id);
    List<InterviewScoreDocument> getAllInterviews();
    Optional<InterviewScoreDocument> updateInterviewScoreDocument(String id, InterviewScoreDocument interviewScoreDocument);
    void deleteInterviewById(String id);
    double computeFinalScore(InterviewScoreDocument interviewScoreDocument);
    Optional<InterviewScoreDocumentDTO> getFormattedInterviewById(String id);
    Optional<InterviewScoreDocumentDTO> getFormattedInterviewByCandidateId(String candidateId);
}
