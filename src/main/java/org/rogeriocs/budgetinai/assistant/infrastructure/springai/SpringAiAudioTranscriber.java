package org.rogeriocs.budgetinai.assistant.infrastructure.springai;

import org.rogeriocs.budgetinai.assistant.domain.AudioTranscriber;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.ai.provider", havingValue = "openai", matchIfMissing = true)
@ConditionalOnBean(TranscriptionModel.class)
public class SpringAiAudioTranscriber implements AudioTranscriber {

    private final TranscriptionModel transcriptionModel;

    public SpringAiAudioTranscriber(TranscriptionModel transcriptionModel) {
        this.transcriptionModel = transcriptionModel;
    }

    @Override
    public String transcribe(byte[] audioContent, String filename) {
        var resource = new ByteArrayResource(audioContent) {
            @Override
            public String getFilename() {
                return filename != null && !filename.isBlank() ? filename : "audio.mp3";
            }
        };
        return transcriptionModel.transcribe(resource);
    }
}
