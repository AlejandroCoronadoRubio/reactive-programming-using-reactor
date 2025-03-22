package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.NetworkException;
import com.learnreactiveprogramming.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MovieReactiveServiceMockTest {

    @Mock
    MovieInfoService movieInfoService;

    @Mock
    ReviewService reviewService;

    @InjectMocks
    MovieReactiveService movieReactiveService;

    @Test
    void getAllMovies() {
        //given
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenCallRealMethod();

        //when
        var value = movieReactiveService.getAllMovies();

        //then
        StepVerifier.create(value)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void getAllMoviesException() {
        //given

        var errorMessage = "Exception occurred in ReviewService";

        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new RuntimeException(errorMessage));

        //when
        var value = movieReactiveService.getAllMovies();

        //then
        StepVerifier.create(value)
                .expectErrorMessage(errorMessage)
                //.expectError(MovieException.class)
                .verify();
    }

    @Test
    void getAllMoviesRetry() {
        //given

        var errorMessage = "Exception occurred in ReviewService";

        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new RuntimeException(errorMessage));

        //when
        var value = movieReactiveService.getAllMoviesRetry();

        //then
        StepVerifier.create(value)
                .expectErrorMessage(errorMessage)
                //.expectError(MovieException.class)
                .verify();

        verify(reviewService, times(4)).retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMoviesRetryWhen1() {
        //given

        var errorMessage = "Exception occurred in ReviewService";

        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new NetworkException(errorMessage));

        //when
        var value = movieReactiveService.getAllMoviesRetryWhen();

        //then
        StepVerifier.create(value)
                .expectErrorMessage(errorMessage)
                //.expectError(MovieException.class)
                .verify();

        verify(reviewService, times(4)).retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMoviesRetryWhen2() {
        //given

        var errorMessage = "Exception occurred in ReviewService";

        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new ServiceException(errorMessage));

        //when
        var value = movieReactiveService.getAllMoviesRetryWhen();

        //then
        StepVerifier.create(value)
                .expectErrorMessage(errorMessage)
                //.expectError(MovieException.class)
                .verify();

        verify(reviewService, times(1)).retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMoviesRepeat() {
        //given

        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenCallRealMethod();

        //when
        var value = movieReactiveService.getAllMoviesRepeat();

        //then
        StepVerifier.create(value)
                .expectNextCount(6)
                //.expectError(MovieException.class)
                .thenCancel()
                .verify();

        verify(reviewService, times(6)).retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMoviesRepeatN() {
        //given

        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenCallRealMethod();

        //when
        var value = movieReactiveService.getAllMoviesRepeat(2);

        //then
        StepVerifier.create(value)
                .expectNextCount(9)
                //.expectError(MovieException.class)
                .verifyComplete();

        verify(reviewService, times(9)).retrieveReviewsFlux(isA(Long.class));
    }


}