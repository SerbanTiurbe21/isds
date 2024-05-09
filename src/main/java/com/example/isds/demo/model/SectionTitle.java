package com.example.isds.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "SectionTitles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionTitle {
    private String id;
    @NonNull
    private String title;
}
