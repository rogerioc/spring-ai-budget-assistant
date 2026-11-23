package org.rogeriocs.budgetinai.assistant.domain;

public interface ChatAssistant {
    String chat(String prompt);
    String chatWithModel(String prompt);
}
