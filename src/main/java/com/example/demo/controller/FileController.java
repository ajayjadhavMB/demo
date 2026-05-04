package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.FileResponse;
import com.example.demo.entity.FileAsset;
import com.example.demo.service.FileStorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/files")
@Tag(name = "File API", description = "Upload files to S3, store metadata in the database, and return access URLs")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload an image or file")
    public ResponseEntity<ApiResponse<FileResponse>> upload(@RequestPart("file") MultipartFile file) throws IOException {
        FileAsset saved = fileStorageService.upload(file);
        FileResponse response = toResponse(saved);
        return ResponseEntity.ok(ApiResponse.<FileResponse>builder()
                .success(true)
                .message("File uploaded successfully")
                .data(response)
                .build());
    }

    @GetMapping
    @Operation(summary = "List all stored files with clickable URLs")
    public ResponseEntity<ApiResponse<List<FileResponse>>> findAll() {
        List<FileResponse> files = fileStorageService.findAll().stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.<List<FileResponse>>builder()
                .success(true)
                .message("Files fetched successfully")
                .data(files)
                .build());
    }

    @GetMapping("/{id}/view")
    @Operation(summary = "View an image or file inline by id")
    public ResponseEntity<Void> view(@PathVariable Long id) {
        FileAsset fileAsset = fileStorageService.getById(id);
        String accessUrl = fileStorageService.createAccessUrl(fileAsset);
        if (accessUrl == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, accessUrl)
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a file by id")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        fileStorageService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("File deleted successfully")
                .data(null)
                .build());
    }

    private FileResponse toResponse(FileAsset fileAsset) {
        return FileResponse.builder()
                .id(fileAsset.getId())
                .originalName(fileAsset.getOriginalName())
                .contentType(fileAsset.getContentType())
                .fileSize(fileAsset.getFileSize())
                .url(fileStorageService.createAccessUrl(fileAsset))
                .createdAt(fileAsset.getCreatedAt())
                .build();
    }
}
