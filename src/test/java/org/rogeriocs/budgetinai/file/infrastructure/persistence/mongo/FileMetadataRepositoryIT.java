package org.rogeriocs.budgetinai.file.infrastructure.persistence.mongo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.rogeriocs.budgetinai.file.domain.FileMetadata;
import org.rogeriocs.budgetinai.file.domain.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.docker.compose.skip.in-tests=false",
        "spring.ai.openai.api-key=test-key-for-context-load"
})
class FileMetadataRepositoryIT {

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Test
    @DisplayName("Deve salvar e recuperar documento com UUID no MongoDB sem erro de CodecConfigurationException")
    void shouldSaveAndRetrieveDocumentWithUUID() {
        var userId = UUID.randomUUID();
        var metadata = new FileMetadata(
                userId,
                "teste.m4a",
                "audio/m4a",
                12345L,
                "/tmp/storage/teste.m4a"
        );

        var saved = fileMetadataRepository.save(metadata);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUserId()).isEqualTo(userId);

        var found = fileMetadataRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(userId);
        assertThat(found.get().getOriginalFilename()).isEqualTo("teste.m4a");

        var userFiles = fileMetadataRepository.findByUserId(userId);
        assertThat(userFiles).isNotEmpty();
        assertThat(userFiles.getFirst().getUserId()).isEqualTo(userId);
    }
}
