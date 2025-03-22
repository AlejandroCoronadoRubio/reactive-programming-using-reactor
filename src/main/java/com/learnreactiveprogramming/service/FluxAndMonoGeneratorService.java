package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

@Slf4j
public class FluxAndMonoGeneratorService {


    public Flux<String> namesFlux() {

        return Flux.fromIterable(List.of("alex", "ben", "chloe")).log(); //db or remote service call
    }

    public Mono<String> namesMono() {
        return Mono.just("alex").log();
    }

    public Flux<String> namesFluxMap() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .doOnNext(name -> System.out.println("Name is: "+name))
                .doOnSubscribe(s -> System.out.println("Subscription is: " + s))
                .doOnComplete(() -> System.out.println("Inside the complete callback"))
                .doFinally(signalType -> System.out.println("Inside finally callback: " + signalType))
                .log();
    }

    public Flux<String> namesFluxImmutability() {
        var namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));
        namesFlux.map(String::toUpperCase);
        return namesFlux;
    }

    public Flux<String> exceptionFlux() {
        return Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("D")).log();
    }

    public Flux<String> fluxOnErrorReturn() {
        return Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .onErrorReturn("D")
                .log();
    }

    public Mono<Object> monoOnErrorReturn() {
        return Mono.just("A")
                .map(ex -> {
                    throw new RuntimeException("Exception occurred");
                })
                .onErrorReturn("abc")
                .log();
    }

    public Flux<Integer> exploreGenerate() {
        return Flux.generate(() -> 1, (state, sink) -> {
            sink.next(state*2);

            if (state == 10) {
                sink.complete();
            }
            return state+1;
        });
    }

    public Mono<Object> exceptionMonoOnErrorMap() {
        return Mono.just("B")
                .map(e -> {
                    throw new RuntimeException("Exception occurred");
                })
                .onErrorMap(ex -> {
                    System.out.println("Exception is: " + ex);
                    return new ReactorException(ex, ex.getMessage());
                })
                .log();
    }

    public Mono<String> exceptionMonoOnErrorContinue(String name) {
        return Mono.just(name)
                .map(n -> {
                    if (n.equals("abc"))
                        throw new RuntimeException("Exception occurred");
                    return n;
                })
                .onErrorContinue((ex, n) -> {
                    System.out.println("Exception is: " + ex);
                    System.out.println("Name is: " + ex);
                })
                .log();
    }

    public Flux<String> fluxDoOnError() {
        return Flux.just("A","B","C")
                .concatWith(Flux.error(new IllegalStateException("Exception occurred")))
                .doOnError(ex -> log.error("Exception is: ", ex))
                .log();
    }

    public Flux<String> fluxOnErrorResume(Exception e) {

        var recoveryFlux = Flux.just("D","E","F");

        return Flux.just("A","B","C")
                .concatWith(Flux.error(e))
                .onErrorResume(ex -> {
                    log.error("Exception is: ", e);
                    if (ex instanceof IllegalStateException)
                        return recoveryFlux;
                    else
                        return Flux.error(e);
                })
                .log();
    }

    public Flux<String> fluxOnErrorContinue() {
        return Flux.just("A","B","C")
                .map(name -> {
                    if (name.equals("B"))
                        throw new IllegalStateException("Exception occurred");
                    return name;
                })
                .concatWith(Flux.just("D"))
                .onErrorContinue((ex, name) -> {
                    log.error("Exception is: ", ex);
                    log.info("Name is: ", name);
                })
                .log();
    }

    public Flux<String> fluxOnErrorMap() {

        return Flux.just("A","B","C")
                .map(name -> {
                    if (name.equals("B"))
                        throw new IllegalStateException("Exception occurred");
                    return name;
                })
                .concatWith(Flux.just("D"))
                .onErrorMap(ex -> {
                    log.error("Exception is: ", ex);
                    return new ReactorException(ex, ex.getMessage());
                })
                .log();
    }

    public Flux<String> fluxOnErrorMapOnOperatorDebug(Exception e) {

        /*return Flux.just("A","B","C")
                .map(name -> {
                    if (name.equals("B"))
                        throw new IllegalStateException("Exception occurred");
                    return name;
                })
                .concatWith(Flux.just("D"))*/
        //return Flux.just("A","B","C")
        return Flux.just("A")
                .concatWith(Flux.error(e))
                //.checkpoint("errorSpot")
                .onErrorMap(ex -> {
                    log.error("Exception is: ", ex);
                    return new ReactorException(ex, ex.getMessage());
                })
                .log();
    }

    public Flux<String> namesFluxFilter(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .filter(name -> name.length() > stringLength)
                .map(name -> name.length() + " - " + name.toUpperCase())
                .log();
    }

    public Mono<String> namesMonoMapFilter(int stringLength) {
        return Mono.just("alex")
                .filter(name -> name.length() > stringLength)
                .map(String::toUpperCase)
                .defaultIfEmpty("empty")
                .log();
    }

    public Mono<String> namesMonoMapFilterSwitchIfEmpty(int stringLength) {

        Function<Mono<String>, Mono<String>> filterMap = name -> name.map(String::toUpperCase);

        var defaultFlux = Mono.just("default").transform(filterMap);

        return Mono.just("alex")
                .filter(name -> name.length() > stringLength)
                .map(String::toUpperCase)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Flux<String> fluxConcat() {

        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");
        var gFlux = Mono.just("G");

        return Flux.concat(abcFlux, defFlux, gFlux).log();
    }

    public Flux<String> fluxConcatWith() {

        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");
        return abcFlux.concatWith(defFlux).log();
    }

    public Flux<String> exploreMerge() {
        var abcFlux = Flux.just("A","B","C").delayElements(Duration.ofMillis(1000));
        var defFlux = Flux.just("D","E","F").delayElements(Duration.ofMillis(1000));
        return abcFlux.mergeWith(defFlux).log();
    }

    public Flux<String> exploreMergeMono() {
        var aFlux = Mono.just("A").delayElement(Duration.ofMillis(1000));
        var bFlux = Mono.just("B").delayElement(Duration.ofMillis(1000));
        return Flux.merge(aFlux, bFlux).log();
    }

    public Flux<String> exploreMergeSequential() {
        var abcFlux = Flux.just("A","B","C").delayElements(Duration.ofMillis(1000));
        var defFlux = Flux.just("D","E","F").delayElements(Duration.ofMillis(1000));
        return Flux.mergeSequential(abcFlux, defFlux).log();
    }

    public Flux<String> exploreZip() {
        var abcFlux = Flux.just("A","B","C").delayElements(Duration.ofMillis(1000));
        var defFlux = Flux.just("D","E","F").delayElements(Duration.ofMillis(1000));

        return Flux.zip(abcFlux, defFlux, (a,b) -> a + b).log();
    }

    public Flux<String> exploreZip1() {
        var abcFlux = Flux.just("A","B","C").delayElements(Duration.ofMillis(1000));
        var defFlux = Flux.just("D","E","F").delayElements(Duration.ofMillis(1000));
        var flux3 = Flux.just("1","2","3");
        var flux4 = Flux.just("4","5","6");

        return Flux.zip(abcFlux,defFlux,flux3,flux4).map(value -> value.getT1() + value.getT2() + value.getT3() + value.getT4()).log();
    }

    public Flux<String> exploreZipWith() {
        var abcFlux = Flux.just("A","B","C").delayElements(Duration.ofMillis(1000));
        var defFlux = Flux.just("D","E","F").delayElements(Duration.ofMillis(1000));
        return abcFlux.zipWith(defFlux, (a,b) -> a + b).log();
    }

    public Mono<String> exploreZipWithMono() {
        var abcFlux = Mono.just("A").delayElement(Duration.ofMillis(1000));
        var defFlux = Mono.just("B").delayElement(Duration.ofMillis(1000));
        return abcFlux.zipWith(defFlux, (a,b) -> a + b).log();
    }

    public Flux<String> namesFluxFlatMapFilter(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > stringLength)
                .flatMap(this::splitString)
                .log();
    }

    public Flux<String> namesFluxFlatMapFilterAsync(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > stringLength)
                .flatMap(this::splitStringWithDelay)
                .log();
    }

    public Flux<String> namesFluxConcatMapFilterAsync(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > stringLength)
                .concatMap(this::splitStringWithDelay)
                .log();
    }

    public Mono<List<String>> namesMonoFlatMap(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(name -> name.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    public Flux<String> namesMonoFlatMapMany(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(name -> name.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();
    }

    public Flux<String> namesFluxTransform(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString);

        var defaultFlux = Flux.just("default").transform(filterMap);

        //return Flux.empty();
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                //.flatMap(this::splitString)
                //.defaultIfEmpty("DEFAULT")
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public List<String> names() {
        delay(1000);
        return List.of("alex","ben","chloe");
    }

    public Mono<String> exploreCreateMono() {
        return Mono.create(sink -> {
            sink.success("alex");
        });
    }

    public Flux<String> exploreHandle() {
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .handle((name,sink) -> {
                    if (name.length() > 3) {
                        sink.next(name.toUpperCase());
                    }
                });
    }

    public Flux<String> exploreCreate() {
        return Flux.create(sink -> {
            CompletableFuture.supplyAsync(this::names)
                    .thenAccept(names -> names.forEach(name -> {
                        sink.next(name);
                        sink.next(name);
                    }))
                    .thenRun(() -> sendEvents(sink));
        });
    }

    public void sendEvents(FluxSink<String> sink) {
            CompletableFuture.supplyAsync(this::names)
                    .thenAccept(names -> names.forEach(sink::next))
                    .thenRun(sink::complete);
    }

    public Flux<String> splitString(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Mono<List<String>> splitStringMono(String name) {
        var charArray = name.split("");
        return Mono.just(List.of(charArray));
    }

    public Flux<String> splitStringWithDelay(String name) {
        var charArray = name.split("");
        //var delay = new Random().nextInt(1000);
        var delay = 100;
        return Flux.fromArray(charArray).delayElements(Duration.ofMillis(delay));
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> System.out.println("Name is: " + name));

        fluxAndMonoGeneratorService.namesMono()
                .subscribe(name -> System.out.println("Mono name is: " + name));

        fluxAndMonoGeneratorService.namesFluxMap()
                .subscribe(name -> System.out.println("Name is: " + name));
    }
}
