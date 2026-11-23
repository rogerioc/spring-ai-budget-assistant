package org.rogeriocs.budgetinai.assistant.infrastructure.http;

import org.rogeriocs.budgetinai.assistant.application.SynthesizeVoiceUseCase;
import org.rogeriocs.budgetinai.assistant.infrastructure.http.request.SynthesizeRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TextToSpeechController {

    private final SynthesizeVoiceUseCase synthesizeVoiceUseCase;

    public TextToSpeechController(SynthesizeVoiceUseCase synthesizeVoiceUseCase) {
        this.synthesizeVoiceUseCase = synthesizeVoiceUseCase;
    }

    @PostMapping(value = {"/sinthesize", "/synthesize"}, produces = "audio/mp3")
    public ResponseEntity<Resource> synthesize(@RequestBody SynthesizeRequest request) {
        byte[] audio = synthesizeVoiceUseCase.execute(request.text());
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
