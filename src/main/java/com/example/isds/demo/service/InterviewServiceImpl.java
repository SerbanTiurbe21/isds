package com.example.isds.demo.service;

import com.example.isds.demo.dto.InterviewScoreDocumentDTO;
import com.example.isds.demo.exception.InterviewScoreDocumentAlreadyExistsException;
import com.example.isds.demo.exception.InterviewNotFoundException;
import com.example.isds.demo.exception.InvalidSectionTitleException;
import com.example.isds.demo.exception.InvalidStatusException;
import com.example.isds.demo.model.*;
import com.example.isds.demo.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.isds.demo.utility.InterviewDocumentFormatter.toDTO;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {
    private final InterviewRepository interviewRepository;
    private final SectionTitleService sectionTitleService;

    @Override
    public InterviewScoreDocument createInterviewScoreDocument(InterviewScoreDocument interviewScoreDocument) {
        validateSectionTitles(interviewScoreDocument.getSections());
        interviewRepository.findByCandidateIdAndRoleAppliedForAndInterviewDate(
                        interviewScoreDocument.getCandidateId(),
                        interviewScoreDocument.getRoleAppliedFor(),
                        interviewScoreDocument.getInterviewDate())
                .ifPresent(existingInterview -> {
                    throw new InterviewScoreDocumentAlreadyExistsException("Interview document already exists for candidate ID: " + interviewScoreDocument.getCandidateId());
                });
        if(interviewScoreDocument.getStatus() != DocumentStatus.NEW){
            throw new InvalidStatusException("Invalid status: " + interviewScoreDocument.getStatus());
        }
        interviewScoreDocument.setFinalScore(computeFinalScore(interviewScoreDocument));
        interviewScoreDocument.setStatus(DocumentStatus.NEW);
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

    @Override
    public void closeInterviewScoreDocument(String id) {
        InterviewScoreDocument interviewScoreDocument = getInterviewById(id);
        boolean hasHRFeedback = false;
        boolean hasDevFeedback = false;

        for (Section section : interviewScoreDocument.getSections()) {
            for (InterviewerFeedback feedback : section.getInterviewers()) {
                if ("HR".equals(feedback.getRole()) || "admin".equals(feedback.getRole())) {
                    hasHRFeedback = true;
                }
                if ("DEVELOPER".equals(feedback.getRole())) {
                    hasDevFeedback = true;
                }

                if (hasHRFeedback && hasDevFeedback) {
                    break;
                }
            }
            if (hasHRFeedback && hasDevFeedback) {
                break;
            }
        }
        lockDocumentIfValid(hasHRFeedback, hasDevFeedback, interviewScoreDocument);
    }

    private void lockDocumentIfValid(boolean hasHRFeedback, boolean hasDevFeedback, InterviewScoreDocument interviewScoreDocument) {
        if (hasHRFeedback && hasDevFeedback) {
            interviewScoreDocument.setStatus(DocumentStatus.LOCKED);
            interviewRepository.save(interviewScoreDocument);
        } else {
            throw new IllegalStateException("Cannot lock the document without feedback from both an HR and a Developer.");
        }
    }

    private void validateSectionTitles(List<Section> sections) {
        List<String> validTitles = sectionTitleService.getAllSectionTitles().stream()
                .map(SectionTitle::getTitle)
                .collect(Collectors.toList());

        for (Section section : sections) {
            if (!validTitles.contains(section.getTitle())) {
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
