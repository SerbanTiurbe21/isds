package com.example.isds.demo.service;

import com.example.isds.demo.dto.InterviewScoreDocumentDTO;
import com.example.isds.demo.model.InterviewScoreDocument;

import java.util.List;

public interface InterviewService {
    InterviewScoreDocument createInterviewScoreDocument(InterviewScoreDocument interviewScoreDocument);
    InterviewScoreDocument getInterviewById(String id);
    List<InterviewScoreDocument> getAllInterviews();
    InterviewScoreDocument updateInterviewScoreDocument(String id, InterviewScoreDocument interviewScoreDocument);
    void deleteInterviewById(String id);
    double computeFinalScore(InterviewScoreDocument interviewScoreDocument);
    InterviewScoreDocumentDTO getFormattedInterviewById(String id);
    InterviewScoreDocumentDTO getFormattedInterviewByCandidateId(String candidateId);
    void closeInterviewScoreDocument(String id);
}
