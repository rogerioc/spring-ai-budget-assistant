package org.rogeriocs.budgetinai.assistant.infrastructure.springai;

import org.rogeriocs.budgetinai.assistant.domain.FinancialAssistant;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;

@Component
@ConditionalOnProperty(name = "app.ai.provider.llm", havingValue = "openai", matchIfMissing = true)
public class SpringAiFinancialAssistant implements FinancialAssistant {

    private final ChatClient chatClient;

    public SpringAiFinancialAssistant(ChatClient.Builder chatClientBuilder,
                                      @Value("classpath:/prompts/system-messsage.st") Resource systemPrompt,
                                      TransactionTools transactionTools) throws IOException {
        this.chatClient = chatClientBuilder
                .defaultSystem(systemPrompt.getContentAsString(Charset.defaultCharset()))
                .defaultTools(transactionTools)
                .build();
    }

    @Override
    public String processRequest(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }
}
