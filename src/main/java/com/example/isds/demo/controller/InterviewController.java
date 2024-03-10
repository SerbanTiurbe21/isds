package com.example.isds.demo.controller;

import com.example.isds.demo.dto.InterviewScoreDocumentDTO;
import com.example.isds.demo.model.InterviewScoreDocument;
import com.example.isds.demo.service.InterviewService;
import com.example.isds.demo.utility.InterviewDocumentFormatter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Interview Documents", description = "Operations related to interview documents")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/interview-documents")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class InterviewController {
    private final InterviewService interviewService;

    @Operation(summary = "Create a new interview document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Interview document created", content = {@Content(schema = @Schema(implementation = InterviewScoreDocument.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Interview document already exists")
    })
    @PreAuthorize("hasRole('ROLE_client-hr')")
    @PostMapping("")
    public Mono<ResponseEntity<InterviewScoreDocument>> createInterview(
            @Parameter(description = "Interview document to be created", required = true) @RequestBody InterviewScoreDocument interviewScoreDocument) {
        InterviewScoreDocument savedInterviewScoreDocument = interviewService.createInterviewScoreDocument(interviewScoreDocument);
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(savedInterviewScoreDocument));
    }

    @Operation(summary = "Get a specific interview document by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interview document found", content = {@Content(schema = @Schema(implementation = InterviewScoreDocumentDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Interview document not found")
    })
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-developer')")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<InterviewScoreDocumentDTO>> getFormattedInterviewById(
            @Parameter(description = "ID of the interview document to be retrieved", required = true) @PathVariable String id) {
        InterviewScoreDocumentDTO formattedInterview = interviewService.getFormattedInterviewById(id);
        return Mono.just(ResponseEntity.ok(formattedInterview));
    }

    @Operation(summary = "Get a specific interview document by the candidate's ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interview document found", content = {@Content(schema = @Schema(implementation = InterviewScoreDocumentDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Interview document not found")
    })
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-developer')")
    @GetMapping("/by-candidate-id/{candidateId}")
    public Mono<ResponseEntity<InterviewScoreDocumentDTO>> getInterviewByCandidateId(
            @Parameter(description = "ID of the candidate to retrieve the interview document for", required = true) @PathVariable String candidateId) {
        InterviewScoreDocumentDTO formattedInterview = interviewService.getFormattedInterviewByCandidateId(candidateId);
        return Mono.just(ResponseEntity.ok(formattedInterview));
    }

    @Operation(summary = "Get all interview documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interview documents found", content = {@Content(schema = @Schema(implementation = InterviewScoreDocumentDTO.class))})
    })
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-developer')")
    @GetMapping
    public Mono<ResponseEntity<List<InterviewScoreDocumentDTO>>> getAllInterviews() {
        List<InterviewScoreDocument> documents = interviewService.getAllInterviews();
        List<InterviewScoreDocumentDTO> dtoDocuments = documents.stream().map(document -> InterviewDocumentFormatter.toDTO(document, interviewService.computeFinalScore(document))).collect(Collectors.toList());
        return Mono.just(ResponseEntity.ok(dtoDocuments));
    }

    @Operation(summary = "Update an existing interview document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interview document updated", content = {@Content(schema = @Schema(implementation = InterviewScoreDocument.class))}),
            @ApiResponse(responseCode = "404", description = "Interview document not found")
    })
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-developer')")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<InterviewScoreDocument>> updateInterview(
            @Parameter(description = "ID of the interview document to be updated", required = true) @PathVariable String id,
            @Parameter(description = "Updated interview document", required = true) @RequestBody InterviewScoreDocument interviewScoreDocumentDetails) {
        InterviewScoreDocument updatedDocument = interviewService.updateInterviewScoreDocument(id, interviewScoreDocumentDetails);
        return Mono.just(ResponseEntity.ok(updatedDocument));
    }

    @Operation(summary = "Delete an existing interview document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Interview document deleted"),
            @ApiResponse(responseCode = "404", description = "Interview document not found")
    })
    @PreAuthorize("hasRole('ROLE_client-hr')")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteInterviewById(
            @Parameter(description = "ID of the interview document to be deleted", required = true) @PathVariable String id) {
        interviewService.deleteInterviewById(id);
        return Mono.just(ResponseEntity.noContent().build());
    }
}