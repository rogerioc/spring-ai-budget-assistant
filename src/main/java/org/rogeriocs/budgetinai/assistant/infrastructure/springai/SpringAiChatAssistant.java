package org.rogeriocs.budgetinai.assistant.infrastructure.springai;

import org.rogeriocs.budgetinai.assistant.domain.ChatAssistant;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;

@Component
public class SpringAiChatAssistant implements ChatAssistant {

    private final ChatClient chatClient;
    private final OpenAiChatModel openAiChatModel;

    public SpringAiChatAssistant(ChatClient chatClient, OpenAiChatModel openAiChatModel) {
        this.chatClient = chatClient;
        this.openAiChatModel = openAiChatModel;
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
        return openAiChatModel.call(prompt);
    }
}
