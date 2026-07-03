package com.patipets.core.application.ports.output;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface ImageStoragePort {
    String upload(MultipartFile file) throws IOException;
    void delete(String url);
}
