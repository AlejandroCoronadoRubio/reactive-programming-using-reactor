package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;
import reactor.tools.agent.ReactorDebugAgent;

import java.time.Duration;
import java.util.List;

public class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFluxTest() {
        //given

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        //then
        StepVerifier.create(namesFlux)
                //.expectNext("alex", "ben", "chloe")
                //.expectNextCount(3)
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void namesMonoTest() {
        //given

        //when
        var namesMono = fluxAndMonoGeneratorService.namesMono();

        //then
        StepVerifier.create(namesMono)
                .expectNext("alex")
                .verifyComplete();
    }

    @Test
    void namesFluxMapTest() {
        //given

        //when
        var namesFluxMap = fluxAndMonoGeneratorService.namesFluxMap();

        //then
        StepVerifier.create(namesFluxMap)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFluxImmutabilityTest() {
        //given

        //when
        var namesFluxImmutability = fluxAndMonoGeneratorService.namesFluxImmutability();

        //then
        StepVerifier.create(namesFluxImmutability)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void namesFluxFilterTest() {
        //given

        //when
        var namesFluxFilter = fluxAndMonoGeneratorService.namesFluxFilter(3);

        //then
        StepVerifier.create(namesFluxFilter)
                .expectNext("4 - ALEX", "5 - CHLOE")
                .verifyComplete();
    }

    @Test
    void namesMonoMapFilterTest() {
        //given

        //when
        var namesMonoMapFilter = fluxAndMonoGeneratorService.namesMonoMapFilter(3);

        //then
        StepVerifier.create(namesMonoMapFilter)
                .expectNext("ALEX")
                .verifyComplete();
    }

    @Test
    void namesMonoFlatMapFilterTest() {
        //given
        int stringLength = 3;

        //when
        var namesFluxFlatMapFilter = fluxAndMonoGeneratorService.namesFluxFlatMapFilter(stringLength);

        //then
        StepVerifier.create(namesFluxFlatMapFilter)
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }

    @Test
    void namesMonoFlatMapFilterAsyncTest() {
        //given

        int stringLength = 3;

        //when
        var namesFluxFlatMapFilterAsync = fluxAndMonoGeneratorService.namesFluxFlatMapFilterAsync(stringLength);

        //then
        StepVerifier.create(namesFluxFlatMapFilterAsync)
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFluxConcatMapFilterAsyncTest() {
        //given

        //when
        var namesFluxConcatMapFilterAsync = fluxAndMonoGeneratorService.namesFluxConcatMapFilterAsync(3);

        //then
        StepVerifier.create(namesFluxConcatMapFilterAsync)
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }

    @Test
    void namesFluxConcatMapFilterAsyncTestVirtualTimer() {
        //given
        VirtualTimeScheduler.getOrSet();

        //when
        var value = fluxAndMonoGeneratorService.namesFluxConcatMapFilterAsync(3);

        //then
        StepVerifier.withVirtualTime(() -> value)
                .thenAwait(Duration.ofSeconds(10))
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }

    @Test
    void namesMonoFlatMap() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.namesMonoFlatMap(3);

        //then
        StepVerifier.create(value)
                .expectNext(List.of("A","L","E","X"))
                .verifyComplete();
    }

    @Test
    void namesMonoFlatMapMany() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.namesMonoFlatMapMany(3);

        //then
        StepVerifier.create(value)
                .expectNext("A","L","E","X")
                .verifyComplete();
    }

    @Test
    void namesFluxTransform() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.namesFluxTransform(3);

        //then
        StepVerifier.create(value)
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }

    @Test
    void namesFluxEmpty() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.namesFluxTransform(6);

        //then
        StepVerifier.create(value)
                .expectNext("D","E","F","A","U","L","T")
                .verifyComplete();
    }

    @Test
    void fluxConcat() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.fluxConcat();

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C","D","E","F","G")
                .verifyComplete();
    }

    @Test
    void fluxConcatWith() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.fluxConcatWith();

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void exploreMerge() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.exploreMerge();

        //then
        StepVerifier.create(value)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void exploreMergeMono() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.exploreMergeMono();

        //then
        StepVerifier.create(value)
                .expectNext("A","B")
                .verifyComplete();
    }

    @Test
    void exploreMergeSequential() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.exploreMergeSequential();

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void exploreZip() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.exploreZip();

        //then
        StepVerifier.create(value)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

    @Test
    void exploreZipWith() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.exploreZipWith();

        //then
        StepVerifier.create(value)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

    @Test
    void exploreZipWithMono() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.exploreZipWithMono();

        //then
        StepVerifier.create(value)
                .expectNext("AB")
                .verifyComplete();
    }

    @Test
    void exploreZip1() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.exploreZip1();

        //then
        StepVerifier.create(value)
                .expectNext("AD14","BE25","CF36")
                .verifyComplete();
    }

    @Test
    void exceptionFlux() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.exceptionFlux();

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C")
                //.expectErrorMessage("Exception occurred")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void fluxOnErrorReturn() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.fluxOnErrorReturn();

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C","D")
                .verifyComplete();
    }

    @Test
    void fluxOnErrorResume() {

        //given
        var e = new IllegalStateException("Not a valid state");

        //when
        var value = fluxAndMonoGeneratorService.fluxOnErrorResume(e);

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void fluxOnErrorResumeCondition() {

        //given
        var e = new RuntimeException("Not a valid state");

        //when
        var value = fluxAndMonoGeneratorService.fluxOnErrorResume(e);

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectError()
                .verify();
    }

    @Test
    void fluxOnErrorContinue() {
        //given
        var e = new RuntimeException("Not a valid state");

        //when
        var value = fluxAndMonoGeneratorService.fluxOnErrorResume(e);

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectError()
                .verify();
    }

    @Test
    void testFluxOnErrorContinue() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.fluxOnErrorContinue();

        //then
        StepVerifier.create(value)
                .expectNext("A","C","D")
                .verifyComplete();
    }

    @Test
    void fluxOnErrorMap() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.fluxOnErrorMap();

        //then
        StepVerifier.create(value)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void fluxOnErrorMapOnOperatorDebug() {

        //given
        //Hooks.onOperatorDebug();

        var exception = new RuntimeException("Not a valid State");

        //when
        var value = fluxAndMonoGeneratorService.fluxOnErrorMapOnOperatorDebug(exception);

        //then
        StepVerifier.create(value)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void fluxOnErrorMapReactorDebugAgent() {

        //given
        //Hooks.onOperatorDebug();
        ReactorDebugAgent.init();
        ReactorDebugAgent.processExistingClasses();
        var exception = new RuntimeException("Not a valid State");

        //when
        var value = fluxAndMonoGeneratorService.fluxOnErrorMapOnOperatorDebug(exception);

        //then
        StepVerifier.create(value)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void fluxDoOnError() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.fluxDoOnError();

        //then
        StepVerifier.create(value)
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    void monoOnErrorReturn() {

        //given

        //when
        var value = fluxAndMonoGeneratorService.monoOnErrorReturn();

        //then
        StepVerifier.create(value)
                .expectNext("abc")
                .verifyComplete();
    }

    @Test
    void exceptionMonoOnErrorMap() {

            //given

            //when
            var value = fluxAndMonoGeneratorService.exceptionMonoOnErrorMap();

            //then
            StepVerifier.create(value)
                    .expectError(ReactorException.class)
                    .verify();
    }

    @Test
    void exceptionMonoOnErrorContinueFail() {
        //given
        String name = "abc";

        //when
        var value = fluxAndMonoGeneratorService.exceptionMonoOnErrorContinue(name);

        //then
        StepVerifier.create(value)
                .verifyComplete();
    }

    @Test
    void exceptionMonoOnErrorContinuePass() {

        //given
        String name = "reactor";

        //when
        var value = fluxAndMonoGeneratorService.exceptionMonoOnErrorContinue(name);

        //then
        StepVerifier.create(value)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    void exploreGenerate() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.exploreGenerate().log();

        //then
        StepVerifier.create(value)
                .expectNextCount(10)
                //.expectNext(2,4,6,8,10,12,14,16,18,20)
                .verifyComplete();
    }

    @Test
    void exploreCreate() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.exploreCreate().log();

        //then
        StepVerifier.create(value)
                .expectNextCount(9)
                //.expectNext(2,4,6,8,10,12,14,16,18,20)
                .verifyComplete();
    }

    @Test
    void exploreCreateMono() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.exploreCreateMono().log();

        //then
        StepVerifier.create(value)
                .expectNext("alex")
                .verifyComplete();
    }

    @Test
    void exploreHandle() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.exploreHandle().log();

        //then
        StepVerifier.create(value)
                .expectNext("ALEX","CHLOE")
                .verifyComplete();
    }
}
