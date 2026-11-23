package org.rogeriocs.budgetinai.assistant.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rogeriocs.budgetinai.assistant.domain.AudioTranscriber;
import org.rogeriocs.budgetinai.assistant.domain.ChatAssistant;
import org.rogeriocs.budgetinai.assistant.domain.VoiceSynthesizer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssistantUseCasesTest {

    @Mock
    private AudioTranscriber audioTranscriber;

    @Mock
    private VoiceSynthesizer voiceSynthesizer;

    @Mock
    private ChatAssistant chatAssistant;

    @Test
    void shouldTranscribeAudio() {
        var useCase = new TranscribeAudioUseCase(audioTranscriber);
        byte[] audio = "audio".getBytes();
        when(audioTranscriber.transcribe(audio, "rec.m4a")).thenReturn("texto transcrito");

        assertThat(useCase.execute(audio, "rec.m4a")).isEqualTo("texto transcrito");
        verify(audioTranscriber).transcribe(audio, "rec.m4a");
    }

    @Test
    void shouldSynthesizeVoice() {
        var useCase = new SynthesizeVoiceUseCase(voiceSynthesizer);
        byte[] expected = "mp3".getBytes();
        when(voiceSynthesizer.synthesize("Olá")).thenReturn(expected);

        assertThat(useCase.execute("Olá")).isEqualTo(expected);
        verify(voiceSynthesizer).synthesize("Olá");
    }

    @Test
    void shouldDelegateChatMethods() {
        var useCase = new ChatUseCase(chatAssistant);
        when(chatAssistant.chat("Oi")).thenReturn("Olá!");
        when(chatAssistant.chatWithModel("Oi")).thenReturn("Olá do modelo!");

        assertThat(useCase.chat("Oi")).isEqualTo("Olá!");
        assertThat(useCase.chatWithModel("Oi")).isEqualTo("Olá do modelo!");
    }
}
