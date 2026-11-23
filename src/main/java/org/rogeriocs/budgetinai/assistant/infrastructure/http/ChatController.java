package org.rogeriocs.budgetinai.assistant.infrastructure.http;

import org.rogeriocs.budgetinai.assistant.application.ChatUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatUseCase chatUseCase;

    public ChatController(ChatUseCase chatUseCase) {
        this.chatUseCase = chatUseCase;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "prompts", defaultValue = "Tell me a joke") String prompt) {
        return chatUseCase.chat(prompt);
    }

    @GetMapping("/chat-model")
    public String chatModel(@RequestParam(name = "prompts", defaultValue = "Tell me a joke") String prompt) {
        return chatUseCase.chatWithModel(prompt);
    }
}
