package org.rogeriocs.budgetinai.file.infrastructure.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rogeriocs.budgetinai.auth.domain.UserRole;
import org.rogeriocs.budgetinai.auth.infrastructure.persistence.entity.User;
import org.rogeriocs.budgetinai.file.domain.FileMetadata;
import org.rogeriocs.budgetinai.file.domain.FileMetadataRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileMetadataControllerTest {

    @Mock
    private FileMetadataRepository fileMetadataRepository;

    @InjectMocks
    private FileMetadataController controller;

    @Test
    @DisplayName("Deve listar arquivos pertencentes ao usuário autenticado")
    void shouldListUserFiles() {
        var userId = UUID.randomUUID();
        var user = new User(userId, "rogerio", "secret", UserRole.ROLE_USER);
        var fileList = List.of(new FileMetadata(userId, "rec.m4a", "audio/m4a", 2048L, "/path/rec.m4a"));

        when(fileMetadataRepository.findByUserId(userId)).thenReturn(fileList);

        var result = controller.listUserFiles(user);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getOriginalFilename()).isEqualTo("rec.m4a");
        verify(fileMetadataRepository).findByUserId(userId);
    }
}
