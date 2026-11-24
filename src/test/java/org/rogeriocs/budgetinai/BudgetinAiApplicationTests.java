package org.rogeriocs.budgetinai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.docker.compose.skip.in-tests=false",
		"spring.ai.openai.api-key=test-key-for-context-load"
})
class BudgetinAiApplicationTests {

	@Test
	void contextLoads() {
	}

}
