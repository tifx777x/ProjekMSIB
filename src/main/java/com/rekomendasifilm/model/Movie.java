package com.rekomendasifilm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Menambahkan ID dengan auto-generated value
    private Long id; // ID unik untuk setiap movie

    private String title;
    private String genres;
    private String overview;
    private String vote_average;
    private String coverUrl;


    // Constructor
    public Movie(String title, String genres, String overview, String vote_average) {
        this.title = title;
        this.genres = genres;
        this.overview = overview;
        this.vote_average = vote_average;
    }


    // Getter dan Setter untuk properti
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Movie{id=" + id + ", title='" + title + "', genres='" + genres + "'}";
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
