package com.rekomendasifilm.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.rekomendasifilm.model.Movie;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class movieService {

    private final String TMDB_API_KEY = "4822c132c84e58653f0d6ac31a477b69";  // Ganti dengan API Key Anda
    private final String TMDB_API_URL = "https://api.themoviedb.org/3";
    private final List<Movie> movieDatabase;

    // Constructor atau inisialisasi database film (misalnya dari CSV atau database)
    public movieService() {
        movieDatabase = new ArrayList<>();
        loadMoviesFromCSV("F:/MSIB/ProjectAkhirMSIB/rekomendasifilmBE/src/main/resources/data/processed_movies.csv");
    }

    // Fungsi untuk memuat data dari CSV
    private void loadMoviesFromCSV(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length < 5) continue; // Skip jika data tidak lengkap
                String title = nextLine[0];
                String genres = nextLine[1];
                String overview = nextLine[2];
                String vote_average = nextLine[3];
                movieDatabase.add(new Movie(title, genres, overview, vote_average));
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failed to load movies from CSV", e);
        }
    }
    public void enrichMovieData(Movie movie) {
        RestTemplate restTemplate = new RestTemplate();
        String searchUrl = TMDB_API_URL + "/search/movie?api_key=" + TMDB_API_KEY + "&query=" + movie.getTitle();

        try {
            String response = restTemplate.getForObject(searchUrl, String.class);
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("results") && jsonResponse.getJSONArray("results").length() > 0) {
                JSONObject firstResult = jsonResponse.getJSONArray("results").getJSONObject(0);

                // Menambahkan data dari TMDB
                if (firstResult.has("poster_path")) {
                    movie.setCoverUrl("https://image.tmdb.org/t/p/w500" + firstResult.getString("poster_path"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fungsi untuk mencari film berdasarkan kata kunci
    public List<Movie> searchMovies(String searchTerm) {
        if (movieDatabase.isEmpty()) {
            throw new RuntimeException("Movie database is empty. Check your CSV file.");
        }
        searchTerm = searchTerm.toLowerCase();
        List<Movie> searchResults = new ArrayList<>();
        for (Movie movie : movieDatabase) {
            if (movie.getTitle().toLowerCase().contains(searchTerm)||movie.getGenres().toLowerCase().contains(searchTerm))
            {
                searchResults.add(movie);
            }
        }
        return searchResults;
    }

    // Mengembalikan semua film
    public List<Movie> getAllMovies() {
        if (movieDatabase.isEmpty()) {
            throw new RuntimeException("Movie database is empty. Check your CSV file.");
        }
        return movieDatabase;
    }
    public Movie getMovieByTitle(String title) {
        return movieDatabase.stream()
                .filter(movie -> movie.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null); // Kembalikan null jika film tidak ditemukan
    }
}