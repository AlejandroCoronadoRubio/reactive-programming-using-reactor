package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import com.learnreactiveprogramming.exception.NetworkException;
import com.learnreactiveprogramming.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Slf4j
public class MovieReactiveService {

    private final MovieInfoService movieInfoService;
    private final ReviewService reviewService;

    private RevenueService revenueService;

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
    }

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService, RevenueService revenueService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
        this.revenueService = revenueService;
    }

    public Flux<Movie> getAllMovies() {
        var moviesInfoFlux =  movieInfoService.retrieveMoviesFlux();

        return moviesInfoFlux
                .flatMap(movieInfo ->  {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
                    return reviewsMono
                            .map(reviewsList -> new Movie(movieInfo, reviewsList)).log();
                })
                .onErrorMap((ex) -> {
                    log.error("Exception is: ", ex);
                    throw new MovieException(ex.getMessage());
                })
                .log();
    }

    public Flux<Movie> getAllMoviesWebClient() {
        var moviesInfoFlux =  movieInfoService.retrieveAllMovieInfoWebClient();

        return moviesInfoFlux
                .flatMap(movieInfo ->  {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFluxWebClient(movieInfo.getMovieInfoId()).collectList();
                    return reviewsMono
                            .map(reviewsList -> new Movie(movieInfo, reviewsList)).log();
                })
                .onErrorMap((ex) -> {
                    log.error("Exception is: ", ex);
                    throw new MovieException(ex.getMessage());
                })
                .log();
    }

    public Flux<Movie> getAllMoviesRetry() {
        var moviesInfoFlux =  movieInfoService.retrieveMoviesFlux();

        return moviesInfoFlux
                .flatMap(movieInfo ->  {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
                    return reviewsMono
                            .map(reviewsList -> new Movie(movieInfo, reviewsList)).log();
                })
                .onErrorMap((ex) -> {
                    log.error("Exception is: ", ex);
                    throw new MovieException(ex.getMessage());
                })
                .retry(3)
                .log();
    }

    public Flux<Movie> getAllMoviesRetryWhen() {
        var moviesInfoFlux =  movieInfoService.retrieveMoviesFlux();

        return moviesInfoFlux
                .flatMap(movieInfo ->  {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
                    return reviewsMono
                            .map(reviewsList -> new Movie(movieInfo, reviewsList)).log();
                })
                .onErrorMap((ex) -> {
                    log.error("Exception is: ", ex);
                    if(ex instanceof NetworkException) {
                        throw new MovieException(ex.getMessage());
                    }
                    throw new ServiceException(ex.getMessage());
                })
                .retryWhen(getRetryBackOffSpec())
                .log();
    }

    public Flux<Movie> getAllMoviesRepeat() {
        var moviesInfoFlux =  movieInfoService.retrieveMoviesFlux();

        return moviesInfoFlux
                .flatMap(movieInfo ->  {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
                    return reviewsMono
                            .map(reviewsList -> new Movie(movieInfo, reviewsList)).log();
                })
                .onErrorMap((ex) -> {
                    log.error("Exception is: ", ex);
                    if(ex instanceof NetworkException) {
                        throw new MovieException(ex.getMessage());
                    }
                    throw new ServiceException(ex.getMessage());
                })
                .retryWhen(getRetryBackOffSpec())
                .repeat()
                .log();
    }

    public Flux<Movie> getAllMoviesRepeat(int repeat) {
        var moviesInfoFlux =  movieInfoService.retrieveMoviesFlux();

        return moviesInfoFlux
                .flatMap(movieInfo ->  {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
                    return reviewsMono
                            .map(reviewsList -> new Movie(movieInfo, reviewsList)).log();
                })
                .onErrorMap((ex) -> {
                    log.error("Exception is: ", ex);
                    if(ex instanceof NetworkException) {
                        throw new MovieException(ex.getMessage());
                    }
                    throw new ServiceException(ex.getMessage());
                })
                .retryWhen(getRetryBackOffSpec())
                .repeat(repeat)
                .log();
    }

    public Mono<Movie> getMovieById(long movieId) {
        var movieInfoMono =  Mono.just(movieInfoService.retrieveMovieUsingId(movieId));
        var reviewsFlux = reviewService.retrieveReviewsFlux(movieId).collectList();
        return movieInfoMono.zipWith(reviewsFlux, (movieInfo, reviews) -> new Movie(movieInfo, reviews)).log();
    }

    public Mono<Movie> getMovieByIdWebClient(long movieId) {
        var movieInfoMono =  movieInfoService.retrieveMovieInfoByIdWebClient(movieId);
        var reviewsFlux = reviewService.retrieveReviewsFluxWebClient(movieId).collectList();
        return movieInfoMono.zipWith(reviewsFlux, (movieInfo, reviews) -> new Movie(movieInfo, reviews)).log();
    }

    public Mono<Movie> getMovieByIdWithRevenue(long movieId) {
        var movieInfoMono =  Mono.just(movieInfoService.retrieveMovieUsingId(movieId));
        var reviewsFlux = reviewService.retrieveReviewsFlux(movieId).collectList();
        var revenueMono = Mono.fromCallable(() -> revenueService.getRevenue(movieId))
                .subscribeOn(Schedulers.boundedElastic());

        return movieInfoMono.zipWith(reviewsFlux, (movieInfo, reviews) -> new Movie(movieInfo, reviews))
                .zipWith(revenueMono, (movie, revenue) -> {
                    movie.setRevenue(revenue);
                    return movie;
                })
                .log();
    }

    public Mono<Movie> getMovieByIdFlatMap(long movieInfoId) {
        var movieInfoMono =  Mono.just(movieInfoService.retrieveMovieUsingId(movieInfoId));
        return movieInfoMono.flatMap(movieInfo -> {
            var reviewsFlux = reviewService.retrieveReviewsFlux(movieInfoId).collectList();
            return reviewsFlux.map(reviewsList -> new Movie(movieInfo, reviewsList)).log();
        });
    }

    private Retry getRetryBackOffSpec() {
        return Retry.backoff(3 , Duration.ofMillis(500))
                .filter(ex -> ex instanceof MovieException)
                .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) ->
                        Exceptions.propagate(retrySignal.failure())));
    }
}
