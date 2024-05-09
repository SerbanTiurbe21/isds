package com.example.isds.demo.controller;

import com.example.isds.demo.model.SectionTitle;
import com.example.isds.demo.service.SectionTitleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SectionTitleControllerTest {
    @Mock
    private SectionTitleService sectionTitleService;

    @InjectMocks
    private SectionTitleController sectionTitleController;
    private SectionTitle mockSectionTitle;

    @BeforeEach
    public void setUp() {
        mockSectionTitle = new SectionTitle();
        mockSectionTitle.setId("1");
        mockSectionTitle.setTitle("Java Comprehension");
    }

    @AfterEach
    public void tearDown() {
        mockSectionTitle = null;
    }

    @Test
    void shouldAddSectionTitle() {
        when(sectionTitleService.createSectionTitle(any(SectionTitle.class))).thenReturn(mockSectionTitle);
        Mono<ResponseEntity<SectionTitle>> response = sectionTitleController.createSectionTitle(mockSectionTitle);
        assert response.block() != null;
        verify(sectionTitleService).createSectionTitle(any(SectionTitle.class));
    }

    @Test
    void shouldGetSectionTitles() {
        when(sectionTitleService.getAllSectionTitles()).thenReturn(Collections.singletonList(mockSectionTitle));
        Mono<ResponseEntity<List<SectionTitle>>> response = sectionTitleController.getAllSectionTitles();
        assert response.block() != null;
        verify(sectionTitleService).getAllSectionTitles();
    }

}
