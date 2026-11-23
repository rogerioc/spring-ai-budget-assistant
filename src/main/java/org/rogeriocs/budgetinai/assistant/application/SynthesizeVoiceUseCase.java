package org.rogeriocs.budgetinai.assistant.application;

import org.rogeriocs.budgetinai.assistant.domain.VoiceSynthesizer;
import org.springframework.stereotype.Service;

@Service
public class SynthesizeVoiceUseCase {

    private final VoiceSynthesizer voiceSynthesizer;

    public SynthesizeVoiceUseCase(VoiceSynthesizer voiceSynthesizer) {
        this.voiceSynthesizer = voiceSynthesizer;
    }

    public byte[] execute(String text) {
        return voiceSynthesizer.synthesize(text);
    }
}
