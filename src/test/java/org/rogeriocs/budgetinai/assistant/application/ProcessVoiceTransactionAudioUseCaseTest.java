package org.rogeriocs.budgetinai.assistant.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rogeriocs.budgetinai.assistant.domain.VoiceSynthesizer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessVoiceTransactionAudioUseCaseTest {

    @Mock
    private ProcessVoiceTransactionTextUseCase textUseCase;

    @Mock
    private VoiceSynthesizer voiceSynthesizer;

    @InjectMocks
    private ProcessVoiceTransactionAudioUseCase useCase;

    @Test
    void shouldProcessAudioToTextAndSynthesizeResponseAudio() {
        byte[] inputAudio = "input-audio".getBytes();
        byte[] outputAudio = "mp3-response".getBytes();

        when(textUseCase.execute(inputAudio, "gasto.m4a"))
                .thenReturn("Gasto de 50 reais salvo.");
        when(voiceSynthesizer.synthesize("Gasto de 50 reais salvo."))
                .thenReturn(outputAudio);

        byte[] result = useCase.execute(inputAudio, "gasto.m4a");

        assertThat(result).isEqualTo(outputAudio);
        verify(textUseCase).execute(inputAudio, "gasto.m4a");
        verify(voiceSynthesizer).synthesize("Gasto de 50 reais salvo.");
    }
}
