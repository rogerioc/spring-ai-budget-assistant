package org.rogeriocs.budgetinai.assistant.domain;

public interface AudioTranscriber {
    String transcribe(byte[] audioContent, String filename);
}
