package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RevieServiceTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies/")
            .build();

    ReviewService reviewService = new ReviewService(webClient);

    @Test
    void retrieveMovieInfoByIdWebClient() {
        //given
        Long movieInfoId = 1L;

        //when
        var value = reviewService.retrieveReviewsFluxWebClient(movieInfoId);

        //then
        StepVerifier.create(value)
                .assertNext(review -> assertEquals("Nolan is the real superhero", review.getComment()))
                .verifyComplete();
    }
}
