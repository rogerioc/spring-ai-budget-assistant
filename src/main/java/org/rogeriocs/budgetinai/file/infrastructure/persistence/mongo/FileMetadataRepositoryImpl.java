package org.rogeriocs.budgetinai.file.infrastructure.persistence.mongo;

import org.rogeriocs.budgetinai.file.domain.FileMetadata;
import org.rogeriocs.budgetinai.file.domain.FileMetadataRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class FileMetadataRepositoryImpl implements FileMetadataRepository {

    private final MongoFileMetadataRepository mongoRepository;

    public FileMetadataRepositoryImpl(MongoFileMetadataRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public FileMetadata save(FileMetadata fileMetadata) {
        var doc = FileMetadataDocument.fromDomain(fileMetadata);
        var saved = mongoRepository.save(doc);
        return saved.toDomain();
    }

    @Override
    public Optional<FileMetadata> findById(String id) {
        return mongoRepository.findById(id).map(FileMetadataDocument::toDomain);
    }

    @Override
    public List<FileMetadata> findByUserId(UUID userId) {
        return mongoRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(FileMetadataDocument::toDomain)
                .toList();
    }
}
