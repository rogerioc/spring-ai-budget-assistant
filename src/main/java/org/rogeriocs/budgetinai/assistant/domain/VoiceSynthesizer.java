package org.rogeriocs.budgetinai.assistant.domain;

public interface VoiceSynthesizer {
    byte[] synthesize(String text);
}
