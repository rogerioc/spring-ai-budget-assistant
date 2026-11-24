package org.rogeriocs.budgetinai.assistant.infrastructure.ollama;

import org.rogeriocs.budgetinai.assistant.domain.AudioTranscriber;
import org.rogeriocs.budgetinai.assistant.domain.VoiceSynthesizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaFallbackAudioConfiguration {

    @Bean
    @ConditionalOnMissingBean(AudioTranscriber.class)
    public AudioTranscriber localAudioTranscriber() {
        return (audioContent, filename) -> {
            throw new UnsupportedOperationException(
                    "Speech-to-Text não está ativo. " +
                    "Para transcrever áudio, ative app.ai.provider.stt=openai e configure a OPENAI_API_KEY no arquivo .env."
            );
        };
    }

    @Bean
    @ConditionalOnMissingBean(VoiceSynthesizer.class)
    public VoiceSynthesizer localVoiceSynthesizer() {
        return text -> {
            throw new UnsupportedOperationException(
                    "Text-to-Speech não está ativo. " +
                    "Para sintetizar resposta em áudio, ative app.ai.provider.tts=openai e configure a OPENAI_API_KEY no arquivo .env."
            );
        };
    }
}
