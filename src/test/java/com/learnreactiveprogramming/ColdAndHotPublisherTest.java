package com.learnreactiveprogramming;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

public class ColdAndHotPublisherTest {


    @Test
    void coldPublisherTest() {
        //given
        var flux = Flux.range(1, 10);

        //when
        flux.subscribe(i -> System.out.println("Subscriber 1: " + i));
        flux.subscribe(i -> System.out.println("Subscriber 2: " + i));

        //then
        /*StepVerifier.create(value)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();*/
    }

    @Test
    void hotPublisherTest() {
        //given
        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));

        //when
        ConnectableFlux<Integer> connectableFlux = flux.publish();
        connectableFlux.connect();

        connectableFlux.subscribe(i -> System.out.println("Subscriber 1: " + i));
        //delay(4000);

        connectableFlux.subscribe(i -> System.out.println("Subscriber 2: " + i));
        delay(10000);

        //then
        /*StepVerifier.create(value)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();*/
    }

    @Test
    void hotPublisherTestAutoConnect() {
        //given
        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));

        //when
        var hotSource = flux.publish().autoConnect(2);

        hotSource.subscribe(i -> System.out.println("Subscriber 1: " + i));
        delay(2000);

        hotSource.subscribe(i -> System.out.println("Subscriber 2: " + i));
        System.out.println("Two subscribers are connected");
        delay(2000);
        hotSource.subscribe(i -> System.out.println("Subscriber 3: " + i));
        delay(10000);

        //then
        /*StepVerifier.create(value)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();*/
    }

    @Test
    void hotPublisherTestRefCount() {
        //given
        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .doOnCancel(() -> System.out.println("Received cancel signal"));

        //when
        var hotSource = flux.publish().refCount(2);

        var disposable = hotSource.subscribe(i -> System.out.println("Subscriber 1: " + i));
        delay(2000);

        var disposable1 = hotSource.subscribe(i -> System.out.println("Subscriber 2: " + i));
        System.out.println("Two subscribers are connected");
        delay(2000);
        disposable.dispose();
        disposable1.dispose();
        hotSource.subscribe(i -> System.out.println("Subscriber 3: " + i));
        delay(2000);
        hotSource.subscribe(i -> System.out.println("Subscriber 4: " + i));
        delay(10000);

        //then
        /*StepVerifier.create(value)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();*/
    }
}
