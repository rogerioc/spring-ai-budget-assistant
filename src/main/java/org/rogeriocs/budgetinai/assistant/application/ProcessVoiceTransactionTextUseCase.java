package org.rogeriocs.budgetinai.assistant.application;

import org.rogeriocs.budgetinai.assistant.domain.AudioTranscriber;
import org.rogeriocs.budgetinai.assistant.domain.FinancialAssistant;
import org.rogeriocs.budgetinai.auth.infrastructure.persistence.entity.User;
import org.rogeriocs.budgetinai.file.domain.FileMetadata;
import org.rogeriocs.budgetinai.file.domain.FileMetadataRepository;
import org.rogeriocs.budgetinai.file.domain.FileStorageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProcessVoiceTransactionTextUseCase {

    private final AudioTranscriber audioTranscriber;
    private final FinancialAssistant financialAssistant;
    private final FileMetadataRepository fileMetadataRepository;
    private final FileStorageService fileStorageService;

    public ProcessVoiceTransactionTextUseCase(AudioTranscriber audioTranscriber,
                                              FinancialAssistant financialAssistant,
                                              FileMetadataRepository fileMetadataRepository,
                                              FileStorageService fileStorageService) {
        this.audioTranscriber = audioTranscriber;
        this.financialAssistant = financialAssistant;
        this.fileMetadataRepository = fileMetadataRepository;
        this.fileStorageService = fileStorageService;
    }

    public String execute(byte[] audioContent, String filename) {
        UUID userId = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .map(User::getId)
                .orElse(null);

        // 1. Armazena o binário físico no Storage
        String storagePath = fileStorageService.store(audioContent, filename);

        // 2. Registra metadados no MongoDB (status: RECEIVED)
        var metadata = new FileMetadata(
                userId,
                filename,
                "audio/m4a",
                audioContent != null ? audioContent.length : 0,
                storagePath
        );
        metadata = fileMetadataRepository.save(metadata);

        try {
            // 3. Transcreve com IA
            var userMessage = audioTranscriber.transcribe(audioContent, filename);
            metadata.markTranscribed(userMessage);
            fileMetadataRepository.save(metadata);

            // 4. Executa LLM e Tool Calling (persistTransaction no MySQL)
            var response = financialAssistant.processRequest(userMessage);
            metadata.markProcessed(response);
            fileMetadataRepository.save(metadata);

            return response;
        } catch (Exception e) {
            metadata.markFailed(e.getMessage());
            fileMetadataRepository.save(metadata);
            throw e;
        }
    }
}
