package com.example.isds.demo.service;

import com.example.isds.demo.dto.InterviewScoreDocumentDTO;
import com.example.isds.demo.model.InterviewScoreDocument;
import com.example.isds.demo.model.Section;
import com.example.isds.demo.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.isds.demo.utility.InterviewDocumentFormatter.toDTO;

@Service
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final List<String> validSectionTitles;

    public InterviewServiceImpl(
            InterviewRepository interviewRepository,
            @Value("${application.section-titles}") List<String> validSectionTitles) {
        this.interviewRepository = interviewRepository;
        this.validSectionTitles = validSectionTitles;
    }

    @Override
    public List<String> getValidSectionTitles() {
        return Collections.unmodifiableList(validSectionTitles);
    }

    @Override
    public InterviewScoreDocument createInterviewScoreDocument(InterviewScoreDocument interviewScoreDocument) {
        validateSectionTitles(interviewScoreDocument.getSections());
        return interviewRepository.save(interviewScoreDocument);
    }

    @Override
    public Optional<InterviewScoreDocument> getInterviewById(String id) {
        return interviewRepository.findById(id);
    }

    @Override
    public List<InterviewScoreDocument> getAllInterviews() {
        return interviewRepository.findAll();
    }

    @Override
    public Optional<InterviewScoreDocument> updateInterviewScoreDocument(String id, InterviewScoreDocument interviewScoreDocument) {
        validateSectionTitles(interviewScoreDocument.getSections());
        return getInterviewById(id).map(interviewScoreDocument1 -> {
            interviewScoreDocument1.setSections(interviewScoreDocument.getSections());
            interviewScoreDocument1.setCandidateIdentifier(interviewScoreDocument.getCandidateIdentifier());
            interviewScoreDocument1.setInterviewDate(interviewScoreDocument.getInterviewDate());
            interviewScoreDocument1.setLastUpdate(interviewScoreDocument.getLastUpdate());
            interviewScoreDocument1.setFinalScore(interviewScoreDocument.getFinalScore());
            interviewScoreDocument1.setRoleAppliedFor(interviewScoreDocument.getRoleAppliedFor());
            return interviewRepository.save(interviewScoreDocument1);
        });
    }

    @Override
    public void deleteInterviewById(String id) {
        interviewRepository.deleteById(id);
    }

    @Override
    public double computeFinalScore(InterviewScoreDocument interviewScoreDocument) {
        if (interviewScoreDocument == null) {
            return 0;
        }

        var sections = interviewScoreDocument.getSections();
        if (sections == null || sections.isEmpty()) {
            return 0;
        }

        var sumAndCount = sections.stream()
                .flatMap(section -> section.getInterviewers().stream())
                .mapToDouble(interviewer -> interviewer.getScore() != null ? interviewer.getScore() : 0)
                .summaryStatistics();

        return sumAndCount.getCount() > 0 ? sumAndCount.getSum() / sumAndCount.getCount() : 0;
    }

    @Override
    public Optional<InterviewScoreDocumentDTO> getFormattedInterviewById(String id) {
        return interviewRepository.findById(id).map(document -> {
            double finalScore = computeFinalScore(document);
            return toDTO(document, finalScore);
        });
    }

    @Override
    public Optional<InterviewScoreDocumentDTO> getFormattedInterviewByCandidateId(String candidateId) {
        return interviewRepository.findByCandidateId(candidateId).map(document -> {
            double finalScore = computeFinalScore(document);
            return toDTO(document, finalScore);
        });
    }

    private void validateSectionTitles(List<Section> sections) {
        for (Section section : sections) {
            if (!getValidSectionTitles().contains(section.getTitle())) {
                throw new IllegalArgumentException("Invalid section title: " + section.getTitle());
            }
        }
    }
}
