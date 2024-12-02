package com.rekomendasifilm.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Map;

@Service
public class FaceEmotionService {

    private static final String FLASK_API_URL = "http://127.0.0.1:5000/detect_emotion";

    // Metode untuk mendapatkan ekspresi wajah dan rekomendasi film
    public Map<String, Object> getEmotionAndRecommendations(byte[] imageBytes) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            // Jika Anda ingin mengirim data gambar dalam bentuk byte[], tambahkan ini
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            HttpEntity<byte[]> entity = new HttpEntity<>(imageBytes, headers);

            // Mengirim data gambar ke Flask API untuk deteksi ekspresi wajah
            ResponseEntity<Map> response = restTemplate.exchange(FLASK_API_URL, HttpMethod.POST, entity, Map.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to Flask API", e);
        }
    }

    // Overload metode untuk mendukung pemanggilan tanpa parameter
    public Map<String, Object> getEmotionAndRecommendations() {
        return getEmotionAndRecommendations(null);
    }
}

