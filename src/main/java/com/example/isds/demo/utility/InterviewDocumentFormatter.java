package com.example.isds.demo.utility;

import com.example.isds.demo.dto.InterviewScoreDocumentDTO;
import com.example.isds.demo.dto.InterviewerFeedbackDTO;
import com.example.isds.demo.dto.SectionDTO;
import com.example.isds.demo.model.InterviewScoreDocument;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class InterviewDocumentFormatter {
    public static final String DATE_PATTERN = "dd.MM.yyyy";

    public static InterviewScoreDocumentDTO toDTO(InterviewScoreDocument document, double finalScore) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        var formattedSections = document
                .getSections()
                .stream()
                .map(section -> {
                    List<InterviewerFeedbackDTO> formattedInterviewers = section
                            .getInterviewers()
                            .stream()

                            .map(interviewer ->
                                    new InterviewerFeedbackDTO(
                                            interviewer.getName(),
                                            interviewer.getRole(),
                                            interviewer.getFeedback(),
                                            interviewer.getScore() != null ? interviewer.getScore() : 0))

                            .collect(Collectors.toList());
                    return new SectionDTO(section.getTitle(), formattedInterviewers);
                })
                .collect(Collectors.toList());

        String formattedInterviewDate = document.getInterviewDate().format(formatter);
        String formattedLastUpdateDate = document.getLastUpdate().format(formatter);

        return new InterviewScoreDocumentDTO(
                formattedInterviewDate,
                formattedLastUpdateDate,
                formattedSections,
                finalScore,
                document.getRoleAppliedFor(),
                document.getCandidateId(),
                document.getStatus());
    }
}
