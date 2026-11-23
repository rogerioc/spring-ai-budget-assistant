package org.rogeriocs.budgetinai.assistant.application;

import org.rogeriocs.budgetinai.assistant.domain.VoiceSynthesizer;
import org.springframework.stereotype.Service;

@Service
public class ProcessVoiceTransactionAudioUseCase {

    private final ProcessVoiceTransactionTextUseCase processVoiceTransactionTextUseCase;
    private final VoiceSynthesizer voiceSynthesizer;

    public ProcessVoiceTransactionAudioUseCase(ProcessVoiceTransactionTextUseCase processVoiceTransactionTextUseCase,
                                               VoiceSynthesizer voiceSynthesizer) {
        this.processVoiceTransactionTextUseCase = processVoiceTransactionTextUseCase;
        this.voiceSynthesizer = voiceSynthesizer;
    }

    public byte[] execute(byte[] audioContent, String filename) {
        var textResponse = processVoiceTransactionTextUseCase.execute(audioContent, filename);
        return voiceSynthesizer.synthesize(textResponse);
    }
}
