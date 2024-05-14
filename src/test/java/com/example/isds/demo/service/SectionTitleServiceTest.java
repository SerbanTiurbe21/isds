package com.example.isds.demo.service;

import com.example.isds.demo.model.SectionTitle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SectionTitleServiceTest {
    @Mock
    private SectionTitleRepository sectionTitleRepository;
    @InjectMocks
    private SectionTitleServiceImpl sectionTitleService;
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
    void shouldCreateSectionTitle() {
        when(sectionTitleRepository.save(any(SectionTitle.class))).thenReturn(mockSectionTitle);
        SectionTitle response = sectionTitleService.createSectionTitle(mockSectionTitle);
        assert response != null;
        verify(sectionTitleRepository).save(any(SectionTitle.class));
    }

//    @Test
//    void shouldT

    @Test
    void shouldGetAllSectionTitles() {
        when(sectionTitleRepository.findAll()).thenReturn(java.util.Collections.singletonList(mockSectionTitle));
        Iterable<SectionTitle> response = sectionTitleService.getAllSectionTitles();
        assert response != null;
        verify(sectionTitleRepository).findAll();
    }
}
