package com.patipets.infrastructure.storage;

import com.patipets.core.application.ports.output.ImageStoragePort;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class SupabaseStorageAdapter implements ImageStoragePort {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String supabaseBucket;

    private final RestTemplate restTemplate;

    public SupabaseStorageAdapter() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(15000);
        this.restTemplate = new RestTemplate(factory);
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(800, 800)
                .outputFormat("jpg")
                .outputQuality(0.75)
                .toOutputStream(baos);
        byte[] compressed = baos.toByteArray();

        String nombre = UUID.randomUUID().toString() + ".jpg";

        String url = supabaseUrl + "/storage/v1/object/" + supabaseBucket + "/" + nombre;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.setContentType(MediaType.IMAGE_JPEG);

        HttpEntity<byte[]> entity = new HttpEntity<>(compressed, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return supabaseUrl + "/storage/v1/object/public/" + supabaseBucket + "/" + nombre;
        }
        throw new RuntimeException("Error al subir imagen a Supabase: " + response.getStatusCode());
    }

    @Override
    public void delete(String url) {
        String marker = "/object/public/" + supabaseBucket + "/";
        int idx = url.indexOf(marker);
        if (idx == -1) {
            return;
        }
        String objectPath = url.substring(idx + marker.length());
        String deleteUrl = supabaseUrl + "/storage/v1/object/" + supabaseBucket + "/" + objectPath;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(deleteUrl, HttpMethod.DELETE, entity, String.class);
        } catch (RestClientException e) {
            // Best-effort: es compensación de una operación ya resuelta, no debe propagar el fallo
        }
    }
}
