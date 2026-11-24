package org.rogeriocs.budgetinai.assistant.infrastructure;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AiProviderConfiguration {

    @Bean
    @Primary
    @ConditionalOnProperty(name = "app.ai.provider.llm", havingValue = "ollama")
    public ChatModel primaryOllamaChatModel(OllamaChatModel ollamaChatModel) {
        return ollamaChatModel;
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "app.ai.provider.llm", havingValue = "openai", matchIfMissing = true)
    public ChatModel primaryOpenAiChatModel(OpenAiChatModel openAiChatModel) {
        return openAiChatModel;
    }
}
