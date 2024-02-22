package com.example.isds.demo.repository;

import com.example.isds.demo.model.InterviewScoreDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterviewRepository extends MongoRepository<InterviewScoreDocument, String> {
    Optional<InterviewScoreDocument> findByCandidateId(String candidateId);
}
