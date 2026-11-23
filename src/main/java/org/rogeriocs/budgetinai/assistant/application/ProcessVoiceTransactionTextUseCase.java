package org.rogeriocs.budgetinai.assistant.application;

import org.rogeriocs.budgetinai.assistant.domain.AudioTranscriber;
import org.rogeriocs.budgetinai.assistant.domain.FinancialAssistant;
import org.springframework.stereotype.Service;

@Service
public class ProcessVoiceTransactionTextUseCase {

    private final AudioTranscriber audioTranscriber;
    private final FinancialAssistant financialAssistant;

    public ProcessVoiceTransactionTextUseCase(AudioTranscriber audioTranscriber,
                                              FinancialAssistant financialAssistant) {
        this.audioTranscriber = audioTranscriber;
        this.financialAssistant = financialAssistant;
    }

    public String execute(byte[] audioContent, String filename) {
        var userMessage = audioTranscriber.transcribe(audioContent, filename);
        return financialAssistant.processRequest(userMessage);
    }
}
