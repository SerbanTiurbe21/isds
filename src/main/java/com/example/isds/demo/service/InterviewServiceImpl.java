package com.example.isds.demo.service;

import com.example.isds.demo.dto.InterviewScoreDocumentDTO;
import com.example.isds.demo.exception.InterviewNotFoundException;
import com.example.isds.demo.exception.InvalidSectionTitleException;
import com.example.isds.demo.model.InterviewScoreDocument;
import com.example.isds.demo.model.Section;
import com.example.isds.demo.repository.InterviewRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
    public InterviewScoreDocument createInterviewScoreDocument(InterviewScoreDocument interviewScoreDocument) {
        validateSectionTitles(interviewScoreDocument.getSections());
        return interviewRepository.save(interviewScoreDocument);
    }

    @Override
    public InterviewScoreDocument getInterviewById(String id) {
        return interviewRepository.findById(id).orElseThrow(() -> new InterviewNotFoundException("Interview not found"));
    }

    @Override
    public List<InterviewScoreDocument> getAllInterviews() {
        return interviewRepository.findAll();
    }

    @Override
    public InterviewScoreDocument updateInterviewScoreDocument(String id, InterviewScoreDocument updatedInterviewScoreDocument) {
        validateSectionTitles(updatedInterviewScoreDocument.getSections());
        InterviewScoreDocument interviewScoreDocument = getInterviewById(id);

        interviewScoreDocument.setSections(updatedInterviewScoreDocument.getSections());
        interviewScoreDocument.setCandidateIdentifier(updatedInterviewScoreDocument.getCandidateIdentifier());
        interviewScoreDocument.setInterviewDate(updatedInterviewScoreDocument.getInterviewDate());
        interviewScoreDocument.setLastUpdate(updatedInterviewScoreDocument.getLastUpdate());
        interviewScoreDocument.setFinalScore(computeFinalScore(updatedInterviewScoreDocument));
        interviewScoreDocument.setRoleAppliedFor(updatedInterviewScoreDocument.getRoleAppliedFor());

        return interviewRepository.save(interviewScoreDocument);
    }

    @Override
    public void deleteInterviewById(String id) {
        interviewRepository.deleteById(id);
    }

    @Override
    public InterviewScoreDocumentDTO getFormattedInterviewById(String id) {
        InterviewScoreDocument document = interviewRepository.findById(id)
                .orElseThrow(() -> new InterviewNotFoundException("Interview document not found with id: " + id));
        double finalScore = computeFinalScore(document);
        return toDTO(document, finalScore);
    }

    @Override
    public InterviewScoreDocumentDTO getFormattedInterviewByCandidateId(String candidateId) {
        InterviewScoreDocument document = interviewRepository.findByCandidateId(candidateId)
                .orElseThrow(() -> new InterviewNotFoundException("Interview document not found for candidate ID: " + candidateId));
        double finalScore = computeFinalScore(document);
        return toDTO(document, finalScore);
    }


    private List<String> getValidSectionTitles() {
        return Collections.unmodifiableList(validSectionTitles);
    }

    private void validateSectionTitles(List<Section> sections) {
        for (Section section : sections) {
            if (!getValidSectionTitles().contains(section.getTitle())) {
                throw new InvalidSectionTitleException("Invalid section title: " + section.getTitle());
            }
        }
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
}
