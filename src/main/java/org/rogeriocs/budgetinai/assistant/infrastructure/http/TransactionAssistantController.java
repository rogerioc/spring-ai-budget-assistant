package org.rogeriocs.budgetinai.assistant.infrastructure.http;

import org.rogeriocs.budgetinai.assistant.application.ProcessVoiceTransactionAudioUseCase;
import org.rogeriocs.budgetinai.assistant.application.ProcessVoiceTransactionTextUseCase;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/transactions")
public class TransactionAssistantController {

    private final ProcessVoiceTransactionTextUseCase processVoiceTransactionTextUseCase;
    private final ProcessVoiceTransactionAudioUseCase processVoiceTransactionAudioUseCase;

    public TransactionAssistantController(ProcessVoiceTransactionTextUseCase processVoiceTransactionTextUseCase,
                                          ProcessVoiceTransactionAudioUseCase processVoiceTransactionAudioUseCase) {
        this.processVoiceTransactionTextUseCase = processVoiceTransactionTextUseCase;
        this.processVoiceTransactionAudioUseCase = processVoiceTransactionAudioUseCase;
    }

    @PostMapping(value = "/ai/text", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String transcribeAndProcess(@RequestParam("file") MultipartFile file) throws IOException {
        return processVoiceTransactionTextUseCase.execute(file.getBytes(), file.getOriginalFilename());
    }

    @PostMapping(value = "/ai/audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "audio/mp3")
    public ResponseEntity<Resource> transcribeProcessAndSpeak(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] audio = processVoiceTransactionAudioUseCase.execute(file.getBytes(), file.getOriginalFilename());
        var resource = new ByteArrayResource(audio);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("audio.mp3")
                                .build()
                                .toString())
                .body(resource);
    }
}
