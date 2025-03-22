package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoSchedulersServiceTest {

    FluxAndMonoSchedulersService fluxAndMonoSchedulersService = new FluxAndMonoSchedulersService();

    @Test
    void explorePublishOn() {

        //given

        //when
        var value = fluxAndMonoSchedulersService.explorePublishOn();

        //then
        StepVerifier.create(value)
                //.expectNext("ALEX", "BEN", "CHLOE","ADAM", "JILL", "JACK")
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void exploreSubscribeOn() {
        //given

        //when
        var value = fluxAndMonoSchedulersService.exploreSubscribeOn();

        //then
        StepVerifier.create(value)
                //.expectNext("ALEX", "BEN", "CHLOE","ADAM", "JILL", "JACK")
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void exploreParallel() {
        //given

        //when
        var value = fluxAndMonoSchedulersService.exploreParallel();

        //then
        StepVerifier.create(value)
                //.expectNext("ALEX", "BEN", "CHLOE","ADAM", "JILL", "JACK")
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void exploreParallel1() {
        //given

        //when
        var value = fluxAndMonoSchedulersService.exploreParallel1();

        //then
        StepVerifier.create(value)
                //.expectNext("ALEX", "BEN", "CHLOE","ADAM", "JILL", "JACK")
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void exploreParallelUsingFlatMap() {
        //given

        //when
        var value = fluxAndMonoSchedulersService.exploreParallelUsingFlatMap();

        //then
        StepVerifier.create(value)
                //.expectNext("ALEX", "BEN", "CHLOE","ADAM", "JILL", "JACK")
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void exploreParallelUsingFlatMap1() {
        //given

        //when
        var value = fluxAndMonoSchedulersService.exploreParallelUsingFlatMap1();

        //then
        StepVerifier.create(value)
                //.expectNext("ALEX", "BEN", "CHLOE","ADAM", "JILL", "JACK")
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void exploreParallelUsingFlatMapSequential() {
        //given

        //when
        var value = fluxAndMonoSchedulersService.exploreParallelUsingFlatMapSequential();

        //then
        StepVerifier.create(value)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();
    }
}