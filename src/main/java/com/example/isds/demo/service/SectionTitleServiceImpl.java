package com.example.isds.demo.service;

import com.example.isds.demo.model.SectionTitle;
import com.example.isds.demo.repository.SectionTitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionTitleServiceImpl implements SectionTitleService{
    private final SectionTitleRepository sectionTitleRepository;
    @Override
    public SectionTitle createSectionTitle(SectionTitle sectionTitle) {
        return sectionTitleRepository.save(sectionTitle);
    }

    @Override
    public List<SectionTitle> getAllSectionTitles() {
        return sectionTitleRepository.findAll();
    }
}
