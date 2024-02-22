package com.example.isds.demo.controller;

import com.example.isds.demo.dto.InterviewScoreDocumentDTO;
import com.example.isds.demo.dto.InterviewerFeedbackDTO;
import com.example.isds.demo.dto.SectionDTO;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InterviewControllerTest {
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

        InterviewerFeedbackDTO interviewerFeedbackDTO = new InterviewerFeedbackDTO("Jane Smith", 85.0, "Good performance");
        SectionDTO sectionDTO = new SectionDTO("Java Comprehension", List.of(interviewerFeedbackDTO));


        mockInterviewDocument = new InterviewScoreDocument();
        mockInterviewDocument.setSections(List.of(section));

        mockInterviewDocumentDTO = new InterviewScoreDocumentDTO(
                "John Doe",
                "2021-08-01",
                "2021-08-01",
                List.of(sectionDTO),
                88.0,
                "Software Engineer",
                "1234"
        );
    }

    @AfterEach
    public void tearDown() {
        mockInterviewDocument = null;
        mockInterviewDocumentDTO = null;
    }

    @Test
    void createInterview_ShouldReturnCreated() {
        when(interviewService.createInterviewScoreDocument(any(InterviewScoreDocument.class))).thenReturn(mockInterviewDocument);

        ResponseEntity<InterviewScoreDocument> response = interviewController.createInterview(mockInterviewDocument);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        assertEquals(mockInterviewDocument, response.getBody());

        verify(interviewService).createInterviewScoreDocument(mockInterviewDocument);
    }

}
