package org.rogeriocs.budgetinai.assistant.infrastructure.ollama;

import org.rogeriocs.budgetinai.assistant.domain.ChatAssistant;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.ai.provider.llm", havingValue = "ollama")
public class OllamaChatAssistant implements ChatAssistant {

    private final ChatClient chatClient;
    private final OllamaChatModel ollamaChatModel;

    public OllamaChatAssistant(OllamaChatModel ollamaChatModel) {
        this.ollamaChatModel = ollamaChatModel;
        this.chatClient = ChatClient.builder(ollamaChatModel).build();
    }

    @Override
    public String chat(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    @Override
    public String chatWithModel(String prompt) {
        return ollamaChatModel.call(prompt);
    }
}
