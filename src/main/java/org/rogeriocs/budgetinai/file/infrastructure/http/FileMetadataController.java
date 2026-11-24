package org.rogeriocs.budgetinai.file.infrastructure.http;

import org.rogeriocs.budgetinai.auth.infrastructure.persistence.entity.User;
import org.rogeriocs.budgetinai.file.domain.FileMetadata;
import org.rogeriocs.budgetinai.file.domain.FileMetadataRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions/ai/files")
public class FileMetadataController {

    private final FileMetadataRepository fileMetadataRepository;

    public FileMetadataController(FileMetadataRepository fileMetadataRepository) {
        this.fileMetadataRepository = fileMetadataRepository;
    }

    @GetMapping
    public List<FileMetadata> listUserFiles(@AuthenticationPrincipal User user) {
        if (user == null || user.getId() == null) {
            return List.of();
        }
        return fileMetadataRepository.findByUserId(user.getId());
    }
}
