package org.rogeriocs.budgetinai.assistant.application;

import org.rogeriocs.budgetinai.assistant.domain.AudioTranscriber;
import org.springframework.stereotype.Service;

@Service
public class TranscribeAudioUseCase {

    private final AudioTranscriber audioTranscriber;

    public TranscribeAudioUseCase(AudioTranscriber audioTranscriber) {
        this.audioTranscriber = audioTranscriber;
    }

    public String execute(byte[] audioContent, String filename) {
        return audioTranscriber.transcribe(audioContent, filename);
    }
}
