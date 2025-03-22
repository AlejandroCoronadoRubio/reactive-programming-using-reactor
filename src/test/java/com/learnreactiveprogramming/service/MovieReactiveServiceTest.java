package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MovieReactiveServiceTest {

    MovieInfoService movieInfoService = new MovieInfoService();
    ReviewService reviewService = new ReviewService();

    RevenueService revenueService = new RevenueService();
    MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService, reviewService, revenueService);


    @Test
    void getAllMovies() {

        //given

        //when
        var value = movieReactiveService.getAllMovies();

        //then
        StepVerifier.create(value)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .assertNext(movie -> {
                    assertEquals("The Dark Knight", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .assertNext(movie -> {
                    assertEquals("Dark Knight Rises", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .verifyComplete();
    }

    @Test
    void getMovieById() {

        //given

        //when
        var value = movieReactiveService.getMovieById(2);

        //then
        StepVerifier.create(value)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .verifyComplete();
    }

    @Test
    void getMovieByIdWithRevenue() {
        //given

        //when
        var value = movieReactiveService.getMovieByIdWithRevenue(2);

        //then
        StepVerifier.create(value)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                    assertNotNull(movie.getRevenue());
                })
                .verifyComplete();
    }

    @Test
    void getMovieByIdFlatMap() {
        //given

        //when
        var value = movieReactiveService.getMovieByIdFlatMap(2);

        //then
        StepVerifier.create(value)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .verifyComplete();
    }
}