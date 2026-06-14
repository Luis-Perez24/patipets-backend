package com.patipets.infrastructure.web.controller;

import com.patipets.core.application.ports.output.ImageStoragePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
public class TestUploadController {

    private final ImageStoragePort imageStoragePort;

    public TestUploadController(ImageStoragePort imageStoragePort) {
        this.imageStoragePort = imageStoragePort;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        try {
            String url = imageStoragePort.upload(file);
            return ResponseEntity.ok(Map.of(
                    "url", url,
                    "mensaje", "Imagen subida exitosamente"
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Error al procesar la imagen: " + e.getMessage()
            ));
        }
    }
}
