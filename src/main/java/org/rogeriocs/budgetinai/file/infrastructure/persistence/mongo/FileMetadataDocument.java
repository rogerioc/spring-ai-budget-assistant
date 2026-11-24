package org.rogeriocs.budgetinai.file.infrastructure.persistence.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rogeriocs.budgetinai.file.domain.FileMetadata;
import org.rogeriocs.budgetinai.file.domain.FileProcessingStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "uploaded_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadataDocument {

    @Id
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

    public static FileMetadataDocument fromDomain(FileMetadata domain) {
        return new FileMetadataDocument(
                domain.getId(),
                domain.getUserId(),
                domain.getOriginalFilename(),
                domain.getContentType(),
                domain.getSizeBytes(),
                domain.getStatus(),
                domain.getStoragePath(),
                domain.getTranscription(),
                domain.getAiResponse(),
                domain.getErrorMessage(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }

    public FileMetadata toDomain() {
        return new FileMetadata(
                this.id,
                this.userId,
                this.originalFilename,
                this.contentType,
                this.sizeBytes,
                this.status,
                this.storagePath,
                this.transcription,
                this.aiResponse,
                this.errorMessage,
                this.createdAt,
                this.updatedAt
        );
    }
}
