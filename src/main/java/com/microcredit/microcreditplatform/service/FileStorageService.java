package com.microcredit.microcreditplatform.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file, String subDirectory) throws IOException {
        // Créer le répertoire s'il n'existe pas
        Path uploadPath = Paths.get(uploadDir, subDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un nom de fichier unique
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;

        // Sauvegarder le fichier
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        // Retourner le chemin relatif
        return subDirectory + "/" + fileName;
    }

    public byte[] loadFile(String filePath) throws IOException {
        Path path = Paths.get(uploadDir, filePath);
        return Files.readAllBytes(path);
    }
}