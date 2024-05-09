package com.example.isds.demo.controller;

import com.example.isds.demo.model.SectionTitle;
import com.example.isds.demo.service.SectionTitleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Section Titles", description = "Operations related to section titles")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/section-titles")
public class SectionTitleController {
    private final SectionTitleService sectionTitleService;

    @Operation(summary = "Get all section titles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved section titles")
    })
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-developer') or hasRole('ROLE_client-admin')")
    @GetMapping("")
    public Mono<ResponseEntity<List<SectionTitle>>> getAllSectionTitles() {
        List<SectionTitle> sectionTitles = sectionTitleService.getAllSectionTitles();
        return Mono.just(ResponseEntity.ok(sectionTitles));
    }

    @Operation(summary = "Create a new section title")
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-developer') or hasRole('ROLE_client-admin')")
    public Mono<ResponseEntity<SectionTitle>> createSectionTitle(@Valid @RequestBody SectionTitle sectionTitle) {
        SectionTitle savedSectionTitle = sectionTitleService.createSectionTitle(sectionTitle);
        return Mono.just(ResponseEntity.ok(savedSectionTitle));
    }
}
