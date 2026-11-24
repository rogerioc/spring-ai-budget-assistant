package org.rogeriocs.budgetinai.file.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FileMetadataTest {

    @Test
    @DisplayName("Deve inicializar com status RECEIVED e transitar estados de processamento de IA")
    void shouldTransitionStatusesCorrectly() {
        var userId = UUID.randomUUID();
        var metadata = new FileMetadata(userId, "audio.m4a", "audio/m4a", 1024L, "/tmp/audio.m4a");

        assertThat(metadata.getStatus()).isEqualTo(FileProcessingStatus.RECEIVED);
        assertThat(metadata.getUserId()).isEqualTo(userId);
        assertThat(metadata.getSizeBytes()).isEqualTo(1024L);

        metadata.markTranscribed("Gastei 15 reais no almoço");
        assertThat(metadata.getStatus()).isEqualTo(FileProcessingStatus.TRANSCRIBED);
        assertThat(metadata.getTranscription()).isEqualTo("Gastei 15 reais no almoço");

        metadata.markProcessed("Transação registrada!");
        assertThat(metadata.getStatus()).isEqualTo(FileProcessingStatus.PROCESSED);
        assertThat(metadata.getAiResponse()).isEqualTo("Transação registrada!");

        metadata.markFailed("Erro na API da OpenAI");
        assertThat(metadata.getStatus()).isEqualTo(FileProcessingStatus.FAILED);
        assertThat(metadata.getErrorMessage()).isEqualTo("Erro na API da OpenAI");
    }
}
