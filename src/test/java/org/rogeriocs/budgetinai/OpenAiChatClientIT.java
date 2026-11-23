package org.rogeriocs.budgetinai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class OpenAiChatClientIT {

    @Autowired
    private OpenAiChatModel openAiChatModel;

    @Test
    void should_executeSum_when_prompted() {
        var chatClient = ChatClient.builder(openAiChatModel)
                .defaultSystem("Você é um matemático.")
                .build();

        var response = chatClient.prompt("Some 10 mais 20. Depois subtraia 30 do resultado anterior. Retorne apenas o resultado final sem explicações")
                .call()
                .content();

        System.out.println("Resposta do ChatClient: " + response);
        assertNotNull(response);
        assertThat(response).contains("0");
    }
}
