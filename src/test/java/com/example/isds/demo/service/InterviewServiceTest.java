package com.example.isds.demo.service;

import com.example.isds.demo.dto.InterviewScoreDocumentDTO;
import com.example.isds.demo.exception.InterviewScoreDocumentAlreadyExistsException;
import com.example.isds.demo.exception.InvalidSectionTitleException;
import com.example.isds.demo.model.InterviewScoreDocument;
import com.example.isds.demo.model.InterviewerFeedback;
import com.example.isds.demo.model.Section;
import com.example.isds.demo.repository.InterviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class InterviewServiceTest {
    @Mock
    private InterviewRepository interviewRepository;
    private InterviewServiceImpl interviewService;
    private InterviewScoreDocument mockInterviewDocument;
    private final List<String> validSectionTitles = List.of(
            "Java Comprehension",
            "Company Values",
            "Distributed Systems Understanding",
            "Integration Tests Comprehension",
            "Automation Tools",
            "Regression Testing",
            "System Design Principles",
            "Cloud Infrastructure and Services",
            "Microservices Architecture",
            "Security Best Practices",
            "Frontend Technologies",
            "Database Design and Optimization",
            "DevOps and CI/CD Pipelines",
            "Data Structures and Algorithms",
            "Performance Tuning",
            "Scalability Strategies",
            "Code Review and Quality Assurance",
            "Agile and Scrum Methodologies",
            "Problem-solving Skills",
            "Project Management",
            "Machine Learning Basics",
            "Blockchain Fundamentals",
            "IoT Concepts",
            "Big Data Technologies",
            "Mobile Development Essentials",
            "User Experience Design",
            "API Design and RESTful Services",
            "Software Development Life Cycle",
            "Business Intelligence",
            "Data Privacy and Compliance",
            "Open Source Contributions",
            "Leadership and Team Collaboration"
    );
    @BeforeEach
    public void setUp() {
        interviewService = new InterviewServiceImpl(interviewRepository, validSectionTitles);

        InterviewerFeedback interviewerFeedback = new InterviewerFeedback("John Doe", "Developer", "Good understanding of Java 8 features", 4.0);
        Section section1 = new Section();
        section1.setTitle("Java Comprehension");
        section1.setInterviewers(List.of(interviewerFeedback));

        mockInterviewDocument = new InterviewScoreDocument();
        mockInterviewDocument.setSections(List.of(section1));
        mockInterviewDocument.setCandidateIdentifier("123");
        mockInterviewDocument.setInterviewDate(LocalDate.now());
        mockInterviewDocument.setLastUpdate(LocalDate.now());
        mockInterviewDocument.setFinalScore(4.0);
        mockInterviewDocument.setRoleAppliedFor("Software Engineer");
        mockInterviewDocument.setCandidateId("123");
    }

    @AfterEach
    public void tearDown() {
        mockInterviewDocument = null;
    }

    @Test
    void createInterviewScoreDocumentShouldSaveAndReturnDocument(){
        when(interviewRepository.save(any(InterviewScoreDocument.class))).thenReturn(mockInterviewDocument);

        InterviewScoreDocument result = interviewService.createInterviewScoreDocument(mockInterviewDocument);

        assertEquals(mockInterviewDocument, result);
        verify(interviewRepository).save(mockInterviewDocument);
    }

    @Test
    void createInterviewScoreDocumentShouldThrowInterviewScoreDocumentAlreadyExistsExceptionWhenDocumentExists() {
        when(interviewRepository.findByCandidateIdAndRoleAppliedForAndInterviewDate(
                mockInterviewDocument.getCandidateId(),
                mockInterviewDocument.getRoleAppliedFor(),
                mockInterviewDocument.getInterviewDate())
        ).thenReturn(Optional.of(mockInterviewDocument));

        assertThrows(InterviewScoreDocumentAlreadyExistsException.class, () -> {
            interviewService.createInterviewScoreDocument(mockInterviewDocument);
        });

        verify(interviewRepository, never()).save(any(InterviewScoreDocument.class));
    }

    @Test
    void getInterviewByIdShouldReturnInterviewWhenPresent(){
        when(interviewRepository.findById("123")).thenReturn(Optional.of(mockInterviewDocument));

        InterviewScoreDocument result = interviewService.getInterviewById("123");

        assertEquals(mockInterviewDocument, result);
        verify(interviewRepository).findById("123");
    }

    @Test
    void getAllInterviewsShouldReturnAllInterviews() {
        when(interviewRepository.findAll()).thenReturn(List.of(mockInterviewDocument));

        List<InterviewScoreDocument> result = interviewService.getAllInterviews();

        assertEquals(List.of(mockInterviewDocument), result);
        verify(interviewRepository).findAll();
    }

    @Test
    void updateInterviewScoreDocumentShouldUpdateAndReturnInterview() {
        when(interviewRepository.findById("123")).thenReturn(Optional.of(mockInterviewDocument));
        when(interviewRepository.save(any(InterviewScoreDocument.class))).thenReturn(mockInterviewDocument);

        InterviewScoreDocument updatedDocument = new InterviewScoreDocument();
        updatedDocument.setSections(mockInterviewDocument.getSections());
        updatedDocument.setCandidateIdentifier("123");
        updatedDocument.setInterviewDate(LocalDate.now());
        updatedDocument.setLastUpdate(LocalDate.now());
        updatedDocument.setFinalScore(4.0);
        updatedDocument.setRoleAppliedFor("Software Engineer");
        updatedDocument.setCandidateId("123");

        InterviewScoreDocument result = interviewService.updateInterviewScoreDocument("123", updatedDocument);

        assertEquals(mockInterviewDocument, result);
        verify(interviewRepository).findById("123");
        verify(interviewRepository).save(mockInterviewDocument);
    }

    @Test
    void deleteInterviewByIdShouldDeleteInterview() {
        doNothing().when(interviewRepository).deleteById("123");

        interviewService.deleteInterviewById("123");

        verify(interviewRepository).deleteById("123");
    }

    @Test
    void getFormattedInterviewByIdShouldReturnFormattedInterview() {
        when(interviewRepository.findById("123")).thenReturn(Optional.of(mockInterviewDocument));

        InterviewScoreDocumentDTO result = interviewService.getFormattedInterviewById("123");

        assertNotNull(result);
        verify(interviewRepository).findById("123");
    }

    @Test
    void getFormattedInterviewByCandidateIdShouldReturnFormattedInterview() {
        when(interviewRepository.findByCandidateId("123")).thenReturn(Optional.ofNullable(mockInterviewDocument));

        InterviewScoreDocumentDTO result = interviewService.getFormattedInterviewByCandidateId("123");

        assertNotNull(result);
        verify(interviewRepository).findByCandidateId("123");
    }

    @Test
    void shouldThrowInvalidSectionTitleExceptionWhenCreatingInterviewScoreDocument() {
        InterviewScoreDocument invalidDocument = new InterviewScoreDocument();
        Section section = new Section();
        section.setTitle("Invalid Title");
        invalidDocument.setSections(List.of(section));

        Exception thrownException = assertThrows(InvalidSectionTitleException.class, () -> {
            interviewService.createInterviewScoreDocument(invalidDocument);
        });

        String expectedMessage = "Invalid section title: Invalid Title";
        assertEquals(expectedMessage, thrownException.getMessage());
    }

    @Test
    void shouldNotComputeFinalScoreIfDocumentIsNull() {
        double finalScore = interviewService.computeFinalScore(null);

        assertEquals(0, finalScore, "Final score should be 0 for a null document");
    }

    @Test
    void shouldNotComputeFinalScoreIfSectionsAreEmpty() {
        InterviewScoreDocument document = new InterviewScoreDocument();
        document.setSections(List.of());

        double finalScore = interviewService.computeFinalScore(document);

        assertEquals(0, finalScore, "Final score should be 0 for a document with no sections");
    }

    @Test
    void shouldNotComputeFinalScoreIfSectionsAreNull() {
        InterviewScoreDocument document = new InterviewScoreDocument();
        document.setSections(null);

        double finalScore = interviewService.computeFinalScore(document);

        assertEquals(0, finalScore, "Final score should be 0 for a document with no sections");
    }
}
