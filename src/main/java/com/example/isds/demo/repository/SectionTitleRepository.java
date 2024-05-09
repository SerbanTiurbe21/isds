package com.example.isds.demo.repository;

import com.example.isds.demo.model.SectionTitle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionTitleRepository extends MongoRepository<SectionTitle, String> {
}
