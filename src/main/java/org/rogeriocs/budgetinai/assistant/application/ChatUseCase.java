package org.rogeriocs.budgetinai.assistant.application;

import org.rogeriocs.budgetinai.assistant.domain.ChatAssistant;
import org.springframework.stereotype.Service;

@Service
public class ChatUseCase {

    private final ChatAssistant chatAssistant;

    public ChatUseCase(ChatAssistant chatAssistant) {
        this.chatAssistant = chatAssistant;
    }

    public String chat(String prompt) {
        return chatAssistant.chat(prompt);
    }

    public String chatWithModel(String prompt) {
        return chatAssistant.chatWithModel(prompt);
    }
}
