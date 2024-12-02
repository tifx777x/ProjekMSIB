package com.rekomendasifilm.service;

import com.rekomendasifilm.model.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MovieCoverService {

    private static final String TMDB_API_KEY = "your_tmdb_api_key"; // Ganti dengan API key TMDB
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3";

    public String getMovieCover(String movieTitle) {
        RestTemplate restTemplate = new RestTemplate();

        // Membangun URL pencarian film berdasarkan judul
        String url = UriComponentsBuilder.fromHttpUrl(TMDB_BASE_URL)
                .path("/search/movie")
                .queryParam("api_key", TMDB_API_KEY)
                .queryParam("query", movieTitle)
                .toUriString();

        // Mengambil respons dari TMDB API
        MovieSearchResponse response = restTemplate.getForObject(url, MovieSearchResponse.class);

        // Mengambil URL gambar pertama dari hasil pencarian
        if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
            String posterPath = response.getResults().get(0).getPosterPath();
            if (posterPath != null) {
                return "https://image.tmdb.org/t/p/w500" + posterPath; // Mengembalikan URL gambar cover
            }
        }

        return null; // Jika tidak ada hasil, kembalikan null
    }

    // Kelas untuk menangani response dari TMDB API
    private static class MovieSearchResponse {
        private List<MovieResult> results;

        public List<MovieResult> getResults() {
            return results;
        }

        public void setResults(List<MovieResult> results) {
            this.results = results;
        }
    }

    private static class MovieResult {
        private String posterPath;

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }
    }
}
