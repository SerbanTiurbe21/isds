package com.example.isds.demo.service;

import com.example.isds.demo.dto.InterviewScoreDocumentDTO;
import com.example.isds.demo.exception.InterviewNotFoundException;
import com.example.isds.demo.exception.InterviewScoreDocumentAlreadyExistsException;
import com.example.isds.demo.exception.InvalidSectionTitleException;
import com.example.isds.demo.exception.InvalidStatusException;
import com.example.isds.demo.model.*;
import com.example.isds.demo.repository.InterviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
class InterviewServiceTest {
    @Mock
    private InterviewRepository interviewRepository;
    @Mock
    private SectionTitleService sectionTitleService;
    @InjectMocks
    private InterviewServiceImpl interviewService;
    private InterviewScoreDocument mockInterviewDocument;

    @BeforeEach
    void setUp() {
        List<SectionTitle> titles = new ArrayList<>();
        titles.add(new SectionTitle("1", "Java Comprehension"));

        lenient().when(sectionTitleService.getAllSectionTitles())
                .thenReturn(List.of(new SectionTitle("1","Java Comprehension"))); // Example of lenient stubbing

        mockInterviewDocument = new InterviewScoreDocument();
        mockInterviewDocument.setId("1");
        mockInterviewDocument.setCandidateId("candidate1");
        mockInterviewDocument.setInterviewDate(LocalDate.now());
        mockInterviewDocument.setLastUpdate(LocalDate.now());
        mockInterviewDocument.setStatus(DocumentStatus.NEW);
        mockInterviewDocument.setRoleAppliedFor("Developer");

        List<InterviewerFeedback> feedbacks = new ArrayList<>();
        feedbacks.add(new InterviewerFeedback("John Doe", "DEVELOPER", "Good", 5.0));
        Section section = new Section("Java Comprehension", feedbacks);
        mockInterviewDocument.setSections(List.of(section));
    }

    @AfterEach
    void tearDown() {
        mockInterviewDocument = null;
    }

    @Test
    void createInterviewScoreDocumentShouldSaveAndReturnDocumentWhenNew() {
        when(interviewRepository.findByCandidateIdAndRoleAppliedForAndInterviewDate(anyString(), anyString(), any(LocalDate.class)))
                .thenReturn(Optional.empty());
        when(interviewRepository.save(any(InterviewScoreDocument.class))).thenReturn(mockInterviewDocument);

        InterviewScoreDocument result = interviewService.createInterviewScoreDocument(mockInterviewDocument);

        assertNotNull(result);
        assertEquals(mockInterviewDocument.getCandidateId(), result.getCandidateId());
        verify(interviewRepository).save(mockInterviewDocument);
    }

    @Test
    void createInterviewScoreDocumentShouldThrowExceptionWhenDocumentExists() {
        when(interviewRepository.findByCandidateIdAndRoleAppliedForAndInterviewDate(anyString(), anyString(), any(LocalDate.class)))
                .thenReturn(Optional.of(mockInterviewDocument));

        assertThrows(InterviewScoreDocumentAlreadyExistsException.class, () ->
                interviewService.createInterviewScoreDocument(mockInterviewDocument));

        verify(interviewRepository, never()).save(any(InterviewScoreDocument.class));
    }

    @Test
    void updateInterviewScoreDocumentShouldUpdateAndReturnUpdatedDocument() {
        when(interviewRepository.findById(anyString())).thenReturn(Optional.of(mockInterviewDocument));
        when(interviewRepository.save(any(InterviewScoreDocument.class))).thenReturn(mockInterviewDocument);

        InterviewScoreDocument updatedDocument = new InterviewScoreDocument();
        updatedDocument.setSections(mockInterviewDocument.getSections());
        updatedDocument.setStatus(DocumentStatus.NEW);

        InterviewScoreDocument result = interviewService.updateInterviewScoreDocument(mockInterviewDocument.getId(), updatedDocument);

        assertNotNull(result);
        assertEquals(DocumentStatus.NEW, result.getStatus());
        verify(interviewRepository).save(mockInterviewDocument);
    }

    @Test
    void getInterviewByIdShouldThrowNotFoundExceptionWhenInterviewDoesNotExist() {
        when(interviewRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(InterviewNotFoundException.class, () -> {
            interviewService.getInterviewById("nonexistent_id");
        });
    }

    @Test
    void deleteInterviewByIdShouldInvokeRepositoryDelete() {
        doNothing().when(interviewRepository).deleteById(anyString());
        interviewService.deleteInterviewById("1");
        verify(interviewRepository).deleteById("1");
    }

    @Test
    void getFormattedInterviewByIdShouldReturnDTO() {
        when(interviewRepository.findById(anyString())).thenReturn(Optional.of(mockInterviewDocument));

        InterviewScoreDocumentDTO dto = interviewService.getFormattedInterviewById("1");

        assertNotNull(dto);
        assertEquals("candidate1", dto.getCandidateId());
    }

    @Test
    void getAllInterviewsShouldReturnAllInterviews() {
        List<InterviewScoreDocument> mockInterviews = List.of(mockInterviewDocument);
        when(interviewRepository.findAll()).thenReturn(mockInterviews);

        List<InterviewScoreDocument> result = interviewService.getAllInterviews();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("candidate1", result.get(0).getCandidateId());
    }

    @Test
    void getFormattedInterviewByCandidateIdShouldReturnDTO() {
        when(interviewRepository.findByCandidateId(anyString())).thenReturn(Optional.of(mockInterviewDocument));

        InterviewScoreDocumentDTO dto = interviewService.getFormattedInterviewByCandidateId("candidate1");

        assertNotNull(dto);
        assertEquals("candidate1", dto.getCandidateId());
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

    @Test
    void shouldThrowExceptionWhenStatusIsNotNew() {
        mockInterviewDocument.setStatus(DocumentStatus.EDITED);

        when(interviewRepository.findByCandidateIdAndRoleAppliedForAndInterviewDate(
                mockInterviewDocument.getCandidateId(),
                mockInterviewDocument.getRoleAppliedFor(),
                mockInterviewDocument.getInterviewDate())
        ).thenReturn(Optional.empty());

        assertThrows(InvalidStatusException.class, () -> {
            interviewService.createInterviewScoreDocument(mockInterviewDocument);
        }, "Should throw InvalidStatusException when the document status is not NEW");

        verify(interviewRepository, never()).save(any(InterviewScoreDocument.class));
    }

    @Test
    void shouldLockDocumentWhenBothHRAndDeveloperFeedbackArePresent() {
        when(interviewRepository.findById("1")).thenReturn(Optional.of(mockInterviewDocument));
        Section section = new Section();
        section.setInterviewers(List.of(
                new InterviewerFeedback("HR", "HR", "Excellent", 5.0),
                new InterviewerFeedback("Dev", "DEVELOPER", "Good", 4.0)
        ));
        mockInterviewDocument.setSections(List.of(section));

        interviewService.closeInterviewScoreDocument("1");

        assertEquals(DocumentStatus.LOCKED, mockInterviewDocument.getStatus());
    }

    @Test
    void shouldNotLockDocumentWhenHRFeedbackIsMissing() {
        when(interviewRepository.findById("1")).thenReturn(Optional.of(mockInterviewDocument));
        Section section = new Section();
        section.setInterviewers(List.of(
                new InterviewerFeedback("Dev", "DEVELOPER", "Good", 4.0)
        ));
        mockInterviewDocument.setSections(List.of(section));

        Exception exception = assertThrows(IllegalStateException.class, () -> interviewService.closeInterviewScoreDocument("1"));
        assertEquals("Cannot lock the document without feedback from both an HR and a Developer.", exception.getMessage());
    }

    @Test
    void shouldNotLockDocumentWhenDeveloperFeedbackIsMissing() {
        when(interviewRepository.findById("1")).thenReturn(Optional.of(mockInterviewDocument));
        Section section = new Section();
        section.setInterviewers(List.of(
                new InterviewerFeedback("HR", "HR", "Excellent", 5.0)
        ));
        mockInterviewDocument.setSections(List.of(section));

        Exception exception = assertThrows(IllegalStateException.class, () -> interviewService.closeInterviewScoreDocument("1"));
        assertEquals("Cannot lock the document without feedback from both an HR and a Developer.", exception.getMessage());
    }

    @Test
    void shouldThrowInvalidSectionTitleExceptionWhenCreatingInterviewScoreDocument() {
        Section section = new Section();
        section.setTitle("Nonexistent Section Title");
        mockInterviewDocument.setSections(List.of(section));

        lenient().when(interviewRepository.findByCandidateIdAndRoleAppliedForAndInterviewDate(
                mockInterviewDocument.getCandidateId(),
                mockInterviewDocument.getRoleAppliedFor(),
                mockInterviewDocument.getInterviewDate())
        ).thenReturn(Optional.empty());

        assertThrows(InvalidSectionTitleException.class, () -> {
            interviewService.createInterviewScoreDocument(mockInterviewDocument);
        }, "Should throw InvalidSectionTitleException when the document contains a non-existent section title");

        verify(interviewRepository, never()).save(any(InterviewScoreDocument.class));
    }

    @Test
    void shouldGetInterviewByCandidateId() {
        when(interviewRepository.findByCandidateId("candidate1")).thenReturn(Optional.of(mockInterviewDocument));

        InterviewScoreDocument result = interviewService.getInterviewByCandidateId("candidate1");

        assertNotNull(result);
        assertEquals("candidate1", result.getCandidateId());
    }

    @Test
    void shouldThrowInterviewNotFoundExceptionWhenInterviewByCandidateIdNotFound() {
        when(interviewRepository.findByCandidateId("candidate1")).thenReturn(Optional.empty());

        assertThrows(InterviewNotFoundException.class, () -> {
            interviewService.getInterviewByCandidateId("candidate1");
        });
    }
}
