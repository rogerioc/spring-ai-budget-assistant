package org.rogeriocs.budgetinai.assistant.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rogeriocs.budgetinai.assistant.domain.AudioTranscriber;
import org.rogeriocs.budgetinai.assistant.domain.FinancialAssistant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessVoiceTransactionTextUseCaseTest {

    @Mock
    private AudioTranscriber audioTranscriber;

    @Mock
    private FinancialAssistant financialAssistant;

    @InjectMocks
    private ProcessVoiceTransactionTextUseCase useCase;

    @Test
    void shouldTranscribeAudioAndDelegateToFinancialAssistant() {
        byte[] audioBytes = "fake-audio".getBytes();
        when(audioTranscriber.transcribe(audioBytes, "gasto.m4a"))
                .thenReturn("Gastei 80 reais no mercado");
        when(financialAssistant.processRequest("Gastei 80 reais no mercado"))
                .thenReturn("Transação registrada com sucesso!");

        var result = useCase.execute(audioBytes, "gasto.m4a");

        assertThat(result).isEqualTo("Transação registrada com sucesso!");
        verify(audioTranscriber).transcribe(audioBytes, "gasto.m4a");
        verify(financialAssistant).processRequest("Gastei 80 reais no mercado");
    }
}
