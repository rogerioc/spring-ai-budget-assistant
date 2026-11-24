package org.rogeriocs.budgetinai.file.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class FileMetadata {
    private String id;
    private UUID userId;
    private String originalFilename;
    private String contentType;
    private long sizeBytes;
    private FileProcessingStatus status;
    private String storagePath;
    private String transcription;
    private String aiResponse;
    private String errorMessage;
    private Instant createdAt;
    private Instant updatedAt;

    public FileMetadata(UUID userId, String originalFilename, String contentType, long sizeBytes, String storagePath) {
        this.userId = userId;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.storagePath = storagePath;
        this.status = FileProcessingStatus.RECEIVED;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void markTranscribed(String transcription) {
        this.transcription = transcription;
        this.status = FileProcessingStatus.TRANSCRIBED;
        this.updatedAt = Instant.now();
    }

    public void markProcessed(String aiResponse) {
        this.aiResponse = aiResponse;
        this.status = FileProcessingStatus.PROCESSED;
        this.updatedAt = Instant.now();
    }

    public void markFailed(String errorMessage) {
        this.errorMessage = errorMessage;
        this.status = FileProcessingStatus.FAILED;
        this.updatedAt = Instant.now();
    }
}
