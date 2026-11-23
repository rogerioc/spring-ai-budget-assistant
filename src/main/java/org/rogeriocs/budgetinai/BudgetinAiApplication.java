package org.rogeriocs.budgetinai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BudgetinAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgetinAiApplication.class, args);
	}

	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		return builder.build();
	}

}

