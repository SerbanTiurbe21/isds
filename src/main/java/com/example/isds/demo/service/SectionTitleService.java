package com.example.isds.demo.service;

import com.example.isds.demo.model.SectionTitle;

import java.util.List;

public interface SectionTitleService {
    SectionTitle createSectionTitle(SectionTitle sectionTitle);
    List<SectionTitle> getAllSectionTitles();
}
