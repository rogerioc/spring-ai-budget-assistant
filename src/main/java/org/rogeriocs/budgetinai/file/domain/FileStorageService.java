package org.rogeriocs.budgetinai.file.domain;

public interface FileStorageService {
    String store(byte[] content, String originalFilename);
    byte[] load(String storagePath);
}
