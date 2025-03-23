package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

class MovieReactiveServiceWebClientTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies/")
            .build();

    private final MovieInfoService movieInfoService = new MovieInfoService(webClient);
    private final ReviewService reviewService = new ReviewService(webClient);
    private final MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService, reviewService);

    @Test
    void getAllMoviesWebClient() {
        //given

        //when
        var value = movieReactiveService.getAllMoviesWebClient();

        //then
        StepVerifier.create(value)
                .expectNextCount(7)
                .verifyComplete();

    }

    @Test
    void getMovieByIdWebClient() {
        //given
        long movieId = 1L;

        //when
        var value = movieReactiveService.getMovieByIdWebClient(movieId);

        //then
        StepVerifier.create(value)
                .expectNextCount(1)
                .verifyComplete();

    }
}