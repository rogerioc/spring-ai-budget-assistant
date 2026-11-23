package org.rogeriocs.budgetinai.assistant.infrastructure.springai;

import org.rogeriocs.budgetinai.assistant.domain.VoiceSynthesizer;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.ai.provider", havingValue = "openai", matchIfMissing = true)
@ConditionalOnBean(TextToSpeechModel.class)
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
