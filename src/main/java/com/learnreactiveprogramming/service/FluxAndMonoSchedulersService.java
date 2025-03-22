package com.learnreactiveprogramming.service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

@Slf4j
public class FluxAndMonoSchedulersService {

    static List<String> namesList = List.of("alex", "ben", "chloe");
    static List<String> namesList1 = List.of("adam", "jill", "jack");

    public Flux<String> explorePublishOn() {
        var namesFlux=  Flux.fromIterable(namesList)
                .publishOn(Schedulers.parallel())
                .map(this::upperCase)
                .log();

        var namesFlux1 = Flux.fromIterable(namesList1)
                .publishOn(Schedulers.boundedElastic())
                .map(this::upperCase)
                .map(s -> {
                    log.info("Names is: " + s);
                    return s;
                })
                .log();

        return namesFlux.mergeWith(namesFlux1);
    }

    public Flux<String> exploreSubscribeOn() {

        Flux<String> namesFlux = getStringFlux()
                .subscribeOn(Schedulers.boundedElastic());

        var namesFlux1 = Flux.fromIterable(namesList1)
                .subscribeOn(Schedulers.boundedElastic())
                .map(this::upperCase)
                .map(s -> {
                    log.info("Names is: " + s);
                    return s;
                })
                .log();

        return namesFlux.mergeWith(namesFlux1);
    }

    public ParallelFlux<String> exploreParallel() {
        log.info("Available processors: {}", Runtime.getRuntime().availableProcessors());

        return Flux.fromIterable(namesList)
                //.publishOn(Schedulers.parallel())
                .parallel()
                .runOn(Schedulers.parallel())
                .map(this::upperCase)
                .log();
    }

    public ParallelFlux<String> exploreParallel1() {
        log.info("Available processors: {}", Runtime.getRuntime().availableProcessors());

        var namesFlux=  Flux.fromIterable(namesList)
                //.publishOn(Schedulers.parallel())
                .parallel()
                .runOn(Schedulers.parallel())
                .map(this::upperCase)
                .log();

        var namesFlux1 = Flux.fromIterable(namesList1)
                //.publishOn(Schedulers.boundedElastic())
                .parallel()
                .runOn(Schedulers.parallel())
                .map(this::upperCase)
                .log();

        return ParallelFlux.from(namesFlux, namesFlux1);
    }

    public Flux<String> exploreParallelUsingFlatMap() {
        log.info("Available processors: {}", Runtime.getRuntime().availableProcessors());

        return Flux.fromIterable(namesList)
                //.publishOn(Schedulers.parallel())
                .flatMap(name -> Mono.just(name)
                        .map(this::upperCase)
                        .subscribeOn(Schedulers.parallel()))
                .log();
    }

    public Flux<String> exploreParallelUsingFlatMapSequential() {
        log.info("Available processors: {}", Runtime.getRuntime().availableProcessors());

        return Flux.fromIterable(namesList)
                //.publishOn(Schedulers.parallel())
                .flatMapSequential(name -> Mono.just(name)
                        .map(this::upperCase)
                        .subscribeOn(Schedulers.parallel()))
                .log();
    }

    public Flux<String> exploreParallelUsingFlatMap1() {
        log.info("Available processors: {}", Runtime.getRuntime().availableProcessors());

        var names = Flux.fromIterable(namesList)
                //.publishOn(Schedulers.parallel())
                .flatMap(name -> Mono.just(name)
                        .map(this::upperCase)
                        .subscribeOn(Schedulers.parallel()))
                .log();

        var names1 = Flux.fromIterable(namesList1)
                //.publishOn(Schedulers.parallel())
                .flatMap(name -> Mono.just(name)
                        .map(this::upperCase)
                        .subscribeOn(Schedulers.parallel()))
                .log();

        return names.mergeWith(names1);
    }

    private Flux<String> getStringFlux() {
        return Flux.fromIterable(namesList)
                .map(this::upperCase)
                .log();
    }

    private String upperCase(String name) {
        delay(1000);
        return name.toUpperCase();
    }

}
