package org.rogeriocs.budgetinai.assistant.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rogeriocs.budgetinai.assistant.domain.AudioTranscriber;
import org.rogeriocs.budgetinai.assistant.domain.FinancialAssistant;
import org.rogeriocs.budgetinai.file.domain.FileMetadata;
import org.rogeriocs.budgetinai.file.domain.FileMetadataRepository;
import org.rogeriocs.budgetinai.file.domain.FileStorageService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessVoiceTransactionTextUseCaseTest {

    @Mock
    private AudioTranscriber audioTranscriber;

    @Mock
    private FinancialAssistant financialAssistant;

    @Mock
    private FileMetadataRepository fileMetadataRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private ProcessVoiceTransactionTextUseCase useCase;

    @Test
    void shouldStoreFileRegisterMetadataAndProcessWithAI() {
        byte[] audioBytes = "fake-audio".getBytes();
        when(fileStorageService.store(audioBytes, "gasto.m4a"))
                .thenReturn("/storage/uploads/fake.m4a");
        when(fileMetadataRepository.save(any(FileMetadata.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(audioTranscriber.transcribe(audioBytes, "gasto.m4a"))
                .thenReturn("Gastei 80 reais no mercado");
        when(financialAssistant.processRequest("Gastei 80 reais no mercado"))
                .thenReturn("Transação registrada com sucesso!");

        var result = useCase.execute(audioBytes, "gasto.m4a");

        assertThat(result).isEqualTo("Transação registrada com sucesso!");
        verify(fileStorageService).store(audioBytes, "gasto.m4a");
        verify(audioTranscriber).transcribe(audioBytes, "gasto.m4a");
        verify(financialAssistant).processRequest("Gastei 80 reais no mercado");
    }
}
