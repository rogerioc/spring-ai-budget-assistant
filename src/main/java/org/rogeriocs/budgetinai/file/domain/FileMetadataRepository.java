package org.rogeriocs.budgetinai.file.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileMetadataRepository {
    FileMetadata save(FileMetadata fileMetadata);
    Optional<FileMetadata> findById(String id);
    List<FileMetadata> findByUserId(UUID userId);
}
