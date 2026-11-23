package org.rogeriocs.budgetinai;

import com.openai.client.OpenAIClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class OpenAiChatModelIT {

    @Autowired
    private OpenAiChatModel chatModel;

    @Test
    void should_receiveResponse_when_chatModelIsCalled() {
//        var options = OpenAiChatOptions.builder()
//                .model("gpt-4o")
//                .temperature(0.8)
//                .build();
//
//        OpenAiChatModel chatModel = OpenAiChatModel.builder()
//                .openAiClient(openAiClient)
//                .options(options)
//                .build();

        String response = chatModel.call("Gere um registro de budgeting, com descrição de gasto, velor em reais e local");

        System.out.println("Resposta da OpenAI: " + response);
        assertNotNull(response);
    }
}