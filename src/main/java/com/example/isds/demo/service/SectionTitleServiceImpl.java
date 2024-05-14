package com.example.isds.demo.service;

import com.example.isds.demo.exception.SectionTitleAlreadyExistsException;
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
        if(sectionTitleRepository.existsByTitle(sectionTitle.getTitle())){
            throw new SectionTitleAlreadyExistsException("Section title already exists");
        }
        return sectionTitleRepository.save(sectionTitle);
    }

    @Override
    public List<SectionTitle> getAllSectionTitles() {
        return sectionTitleRepository.findAll();
    }
}
