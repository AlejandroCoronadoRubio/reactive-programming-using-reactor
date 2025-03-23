package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieInfoServiceTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies/")
            .build();
    MovieInfoService movieInfoService = new MovieInfoService(webClient);
    @Test
    void retrieveAllMovieInfoWebClient() {
        //given

        //when
        var value = movieInfoService.retrieveAllMovieInfoWebClient();

        //then
        StepVerifier.create(value)
                .expectNextCount(7)
                .verifyComplete();
    }

    @Test
    void retrieveMovieInfoByIdWebClient() {
        //given
        Long movieInfoId = 1L;

        //when
        var value = movieInfoService.retrieveMovieInfoByIdWebClient(movieInfoId);

        //then
        StepVerifier.create(value)
                .assertNext( movieInfo ->
                        assertEquals("Batman Begins", movieInfo.getName())

                )
                .verifyComplete();
    }
}