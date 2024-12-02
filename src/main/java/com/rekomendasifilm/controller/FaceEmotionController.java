package com.rekomendasifilm.controller;

import com.rekomendasifilm.model.Movie;
import com.rekomendasifilm.service.FaceEmotionService;
import com.rekomendasifilm.service.movieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class FaceEmotionController {

    @Autowired
    private FaceEmotionService faceEmotionService;

    @Autowired
    private movieService movieService;

    // Halaman Utama dengan Rekomendasi Berdasarkan Ekspresi
    @GetMapping("/")
    public String getRecommendations(Model model) {
        // Deteksi ekspresi wajah dan rekomendasikan film hanya sekali ketika halaman pertama kali dibuka
        Map<String, Object> response = faceEmotionService.getEmotionAndRecommendations(null);

        if (response != null && response.containsKey("emotion") && response.containsKey("recommended_movies")) {
            model.addAttribute("recommendedMovies", response.get("recommended_movies"));
            model.addAttribute("emotion", response.get("emotion"));
        } else {
            model.addAttribute("error", "Unable to fetch data from the Flask API.");
        }

        return "index"; // Kembali ke halaman utama
    }

    // Halaman Pencarian Film
    @GetMapping("/search")
    public String searchMovies(Model model, @RequestParam String searchTerm) {
        List<Movie> searchResults = movieService.searchMovies(searchTerm);

        // Menambahkan URL cover film untuk setiap hasil pencarian
        // Enrich setiap movie dengan data dari TMDB (cover poster, dll.)
        for (Movie movie : searchResults) {
            movieService.enrichMovieData(movie);  // Menambahkan cover poster dan data lainnya
        }

        model.addAttribute("searchResults", searchResults);
        model.addAttribute("searchTerm", searchTerm);
        return "search";
    }

    @GetMapping("/movie/{title}")
    public String getMovieDetails(@PathVariable String title, Model model) {
        Movie movie = movieService.getMovieByTitle(title); // Mendapatkan film berdasarkan judul
        if (movie == null) {
            return "redirect:/";  // Jika film tidak ditemukan, alihkan ke halaman utama
        }
        model.addAttribute("movie", movie);
        return "movieDetails"; // Nama template untuk halaman detail film
    }

}
