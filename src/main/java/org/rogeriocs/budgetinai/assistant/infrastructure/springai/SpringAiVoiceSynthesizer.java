package org.rogeriocs.budgetinai.assistant.infrastructure.springai;

import org.rogeriocs.budgetinai.assistant.domain.VoiceSynthesizer;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.stereotype.Component;

@Component
public class SpringAiVoiceSynthesizer implements VoiceSynthesizer {

    private final TextToSpeechModel textToSpeechModel;

    public SpringAiVoiceSynthesizer(TextToSpeechModel textToSpeechModel) {
        this.textToSpeechModel = textToSpeechModel;
    }

    @Override
    public byte[] synthesize(String text) {
        return textToSpeechModel.call(text);
    }
}
