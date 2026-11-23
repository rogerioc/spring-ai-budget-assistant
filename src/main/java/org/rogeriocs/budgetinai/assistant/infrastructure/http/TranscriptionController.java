package org.rogeriocs.budgetinai.assistant.infrastructure.http;

import org.rogeriocs.budgetinai.assistant.application.TranscribeAudioUseCase;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class TranscriptionController {

    private final TranscribeAudioUseCase transcribeAudioUseCase;

    public TranscriptionController(TranscribeAudioUseCase transcribeAudioUseCase) {
        this.transcribeAudioUseCase = transcribeAudioUseCase;
    }

    @PostMapping(value = "/transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String transcribe(@RequestParam("file") MultipartFile file) throws IOException {
        return transcribeAudioUseCase.execute(file.getBytes(), file.getOriginalFilename());
    }
}
