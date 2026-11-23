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
                    "Speech-to-Text não é suportado nativamente pelo Ollama. " +
                    "Para processar áudio, configure sua OPENAI_API_KEY no arquivo .env."
            );
        };
    }

    @Bean
    @ConditionalOnMissingBean(VoiceSynthesizer.class)
    public VoiceSynthesizer localVoiceSynthesizer() {
        return text -> {
            throw new UnsupportedOperationException(
                    "Text-to-Speech não é suportado nativamente pelo Ollama. " +
                    "Para gerar áudio, configure sua OPENAI_API_KEY no arquivo .env."
            );
        };
    }
}
