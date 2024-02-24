package com.example.isds.demo.repository;

import com.example.isds.demo.model.InterviewScoreDocument;
import com.example.isds.demo.model.InterviewerFeedback;
import com.example.isds.demo.model.Section;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class InterviewRepositoryTest {
    @Autowired
    private InterviewRepository interviewRepository;
    private InterviewScoreDocument mockInterviewDocument;

    @BeforeEach
    public void setUp() {
        InterviewerFeedback interviewerFeedback = new InterviewerFeedback("John Doe", "Developer", "Good understanding of Java 8 features", 4.0);
        Section section = new Section();
        section.setTitle("Java Comprehension");
        section.setInterviewers(List.of(interviewerFeedback));

        mockInterviewDocument = new InterviewScoreDocument();
        mockInterviewDocument.setSections(List.of(section));
        mockInterviewDocument.setCandidateIdentifier("123");
        mockInterviewDocument.setInterviewDate(LocalDate.now());
        mockInterviewDocument.setLastUpdate(LocalDate.now());
        mockInterviewDocument.setFinalScore(4.0);
        mockInterviewDocument.setRoleAppliedFor("Software Engineer");
        mockInterviewDocument.setCandidateId("123");

        interviewRepository.save(mockInterviewDocument);
    }

    @AfterEach
    public void tearDown() {
        interviewRepository.deleteAll();
    }

    @Test
    void findByCandidateIdShouldReturnDocument() {
        String candidateId = mockInterviewDocument.getCandidateId();
        Optional<InterviewScoreDocument> result = interviewRepository.findByCandidateId(candidateId);
        assertTrue(result.isPresent(), "Interview document should be found by candidate ID");
    }

    @Test
    void getInterviewByIdShouldReturnDocument() {
        String id = mockInterviewDocument.getId();
        Optional<InterviewScoreDocument> result = interviewRepository.findById(id);
        assertTrue(result.isPresent(), "Interview document should be found by ID");
    }

    @Test
    void findAllShouldReturnAllDocuments() {
        List<InterviewScoreDocument> result = interviewRepository.findAll();
        assertFalse(result.isEmpty(), "Interview documents should be found");
    }

    @Test
    void deleteByIdShouldDeleteDocument() {
        String id = mockInterviewDocument.getId();
        interviewRepository.deleteById(id);
        Optional<InterviewScoreDocument> result = interviewRepository.findById(id);
        assertFalse(result.isPresent(), "Interview document should be deleted");
    }

    @Test
    void getFormattedInterviewByCandidateIdShouldReturnDocument() {
        String candidateId = mockInterviewDocument.getCandidateId();
        Optional<InterviewScoreDocument> result = interviewRepository.findByCandidateId(candidateId);
        assertTrue(result.isPresent(), "Interview document should be found by candidate ID");
    }
}
