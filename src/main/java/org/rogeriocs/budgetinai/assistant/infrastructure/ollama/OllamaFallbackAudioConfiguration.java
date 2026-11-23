package org.rogeriocs.budgetinai.assistant.infrastructure.ollama;

import org.rogeriocs.budgetinai.assistant.domain.AudioTranscriber;
import org.rogeriocs.budgetinai.assistant.domain.VoiceSynthesizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "app.ai.provider", havingValue = "ollama")
public class OllamaFallbackAudioConfiguration {

    @Bean
    public AudioTranscriber localAudioTranscriber() {
        return (audioContent, filename) -> {
            throw new UnsupportedOperationException(
                    "Speech-to-Text não é suportado nativamente pelo Ollama. " +
                    "Use o endpoint de texto (/api/chat) ou configure a OpenAI (app.ai.provider=openai)."
            );
        };
    }

    @Bean
    public VoiceSynthesizer localVoiceSynthesizer() {
        return text -> {
            throw new UnsupportedOperationException(
                    "Text-to-Speech não é suportado nativamente pelo Ollama. " +
                    "Configure a OpenAI (app.ai.provider=openai) para síntese de voz."
            );
        };
    }
}
