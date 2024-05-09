package com.example.isds.demo.controller;

import com.example.isds.demo.dto.InterviewScoreDocumentDTO;
import com.example.isds.demo.dto.InterviewerFeedbackDTO;
import com.example.isds.demo.dto.SectionDTO;
import com.example.isds.demo.exception.InterviewNotFoundException;
import com.example.isds.demo.model.DocumentStatus;
import com.example.isds.demo.model.InterviewScoreDocument;
import com.example.isds.demo.model.InterviewerFeedback;
import com.example.isds.demo.model.Section;
import com.example.isds.demo.service.InterviewService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class InterviewControllerTest {
    @Mock
    private InterviewService interviewService;
    @InjectMocks
    private InterviewController interviewController;
    private InterviewScoreDocument mockInterviewDocument;
    private InterviewScoreDocumentDTO mockInterviewDocumentDTO;

    @BeforeEach
    public void setUp() {
        InterviewerFeedback interviewerFeedback = new InterviewerFeedback("Jane Smith", "hr", "Good performance", 85.0);
        Section section = new Section();
        section.setTitle("Java Comprehension");
        section.setInterviewers(List.of(interviewerFeedback));

        InterviewerFeedbackDTO interviewerFeedbackDTO = new InterviewerFeedbackDTO("Jane Smith", "DEV", "Good performance", 85.0);
        SectionDTO sectionDTO = new SectionDTO("Java Comprehension", List.of(interviewerFeedbackDTO));


        mockInterviewDocument = new InterviewScoreDocument();
        mockInterviewDocument.setSections(List.of(section));
        mockInterviewDocument.setRoleAppliedFor("Software Engineer");
        mockInterviewDocument.setCandidateId("1234");
        mockInterviewDocument.setInterviewDate(LocalDate.parse("2021-08-01"));
        mockInterviewDocument.setLastUpdate(LocalDate.parse("2021-08-01"));

        mockInterviewDocumentDTO = new InterviewScoreDocumentDTO(
                "2021-08-01",
                "2021-08-01",
                List.of(sectionDTO),
                85.0,
                "Software Engineer",
                "1234",
                DocumentStatus.NEW
        );
    }

    @AfterEach
    public void tearDown() {
        mockInterviewDocument = null;
        mockInterviewDocumentDTO = null;
    }

    @Test
    void createInterviewShouldReturnCreatedInterview() {
        when(interviewService.createInterviewScoreDocument(any(InterviewScoreDocument.class))).thenReturn(mockInterviewDocument);

        Mono<ResponseEntity<InterviewScoreDocument>> response = interviewController.createInterview(mockInterviewDocument);

        assertEquals(HttpStatus.CREATED.value(), Objects.requireNonNull(response.block()).getStatusCode().value());
        assertEquals(mockInterviewDocument, Objects.requireNonNull(response.block()).getBody());

        verify(interviewService).createInterviewScoreDocument(mockInterviewDocument);
    }

    @Test
    void getFormattedInterviewByIdShouldReturnInterviewWhenFound() {
        when(interviewService.getFormattedInterviewById("1234")).thenReturn(mockInterviewDocumentDTO);

        Mono<ResponseEntity<InterviewScoreDocumentDTO>> response = interviewController.getFormattedInterviewById("1234");

        assertEquals(OK.value(), Objects.requireNonNull(response.block()).getStatusCode().value());
        assertEquals(mockInterviewDocumentDTO, Objects.requireNonNull(response.block()).getBody());

        verify(interviewService).getFormattedInterviewById("1234");
    }

    @Test
    void getFormattedInterviewByIdShouldThrowInterviewNotFoundExceptionWhenNotFound() {
        String interviewId = "non-existent-id";
        when(interviewService.getFormattedInterviewById(interviewId)).thenThrow(new InterviewNotFoundException("Interview document not found with id: " + interviewId));

        Exception exception = assertThrows(InterviewNotFoundException.class, () -> {
            interviewController.getFormattedInterviewById(interviewId);
        });

        assertEquals("Interview document not found with id: non-existent-id", exception.getMessage());

        verify(interviewService).getFormattedInterviewById(interviewId);
    }


    @Test
    void getInterviewByCandidateIdShouldReturnInterviewWhenFound() {
        when(interviewService.getFormattedInterviewByCandidateId("1234")).thenReturn(mockInterviewDocumentDTO);

        Mono<ResponseEntity<InterviewScoreDocumentDTO>> response = interviewController.getInterviewByCandidateId("1234");

        assertEquals(OK.value(), Objects.requireNonNull(response.block()).getStatusCode().value());
        assertEquals(mockInterviewDocumentDTO, Objects.requireNonNull(response.block()).getBody());

        verify(interviewService).getFormattedInterviewByCandidateId("1234");
    }

    @Test
    void getInterviewByCandidateIdShouldThrowInterviewNotFoundException() {
        String candidateId = "non-existent-id";
        when(interviewService.getFormattedInterviewByCandidateId(candidateId)).thenThrow(new InterviewNotFoundException("Interview document not found for candidate ID: " + candidateId));

        Exception exception = assertThrows(InterviewNotFoundException.class, () -> {
            interviewController.getInterviewByCandidateId(candidateId);
        });

        assertEquals("Interview document not found for candidate ID: non-existent-id", exception.getMessage());

        verify(interviewService).getFormattedInterviewByCandidateId(candidateId);
    }

    @Test
    void getAllInterviewsShouldReturnListOfInterviews() {
        when(interviewService.getAllInterviews()).thenReturn(Collections.singletonList(mockInterviewDocument));

        Mono<ResponseEntity<List<InterviewScoreDocumentDTO>>> response = interviewController.getAllInterviews();

        assertEquals(OK, Objects.requireNonNull(response.block()).getStatusCode());
        assertNotNull(Objects.requireNonNull(response.block()).getBody());
        assertEquals(1, Objects.requireNonNull(Objects.requireNonNull(response.block()).getBody()).size());
    }

    @Test
    void getAllInterviewsShouldReturnEmptyListWhenNoInterviews() {
        when(interviewService.getAllInterviews()).thenReturn(Collections.emptyList());

        Mono<ResponseEntity<List<InterviewScoreDocumentDTO>>> response = interviewController.getAllInterviews();

        assertEquals(OK, Objects.requireNonNull(response.block()).getStatusCode());
        assertNotNull(Objects.requireNonNull(response.block()).getBody());
        assertEquals(0, Objects.requireNonNull(Objects.requireNonNull(response.block()).getBody()).size());
    }

    @Test
    void updateInterviewShouldReturnUpdatedInterviewWhenFound() {
        String id = "1234";
        when(interviewService.updateInterviewScoreDocument(eq(id), any(InterviewScoreDocument.class)))
                .thenReturn(mockInterviewDocument);

        Mono<ResponseEntity<InterviewScoreDocument>> response = interviewController.updateInterview(id, mockInterviewDocument);

        assertEquals(HttpStatus.OK, Objects.requireNonNull(response.block()).getStatusCode());
        assertEquals(mockInterviewDocument, Objects.requireNonNull(response.block()).getBody());
        verify(interviewService).updateInterviewScoreDocument(id, mockInterviewDocument);
    }

    @Test
    void updateInterviewShouldThrowInterviewNotFoundException() {
        String id = "non-existent-id";
        when(interviewService.updateInterviewScoreDocument(eq(id), any(InterviewScoreDocument.class)))
                .thenThrow(new InterviewNotFoundException("Interview document not found with id: " + id));

        Exception exception = assertThrows(InterviewNotFoundException.class, () -> {
            interviewController.updateInterview(id, mockInterviewDocument);
        });

        assertEquals("Interview document not found with id: non-existent-id", exception.getMessage());
        verify(interviewService).updateInterviewScoreDocument(id, mockInterviewDocument);
    }

    @Test
    void deleteInterviewByIdShouldInvokeServiceMethodWhenInterviewExists() {
        String id = "1234";
        doNothing().when(interviewService).deleteInterviewById(id);

        Mono<ResponseEntity<Void>> response = interviewController.deleteInterviewById(id);

        assertEquals(HttpStatus.NO_CONTENT, Objects.requireNonNull(response.block()).getStatusCode());
        verify(interviewService).deleteInterviewById(id);
    }

    @Test
    void shouldCloseInterview() {
        String id = "1234";
        doNothing().when(interviewService).closeInterviewScoreDocument(id);

        Mono<ResponseEntity<Void>> response = interviewController.closeInterviewById(id);

        assertEquals(HttpStatus.NO_CONTENT, Objects.requireNonNull(response.block()).getStatusCode());
        verify(interviewService).closeInterviewScoreDocument(id);
    }
}