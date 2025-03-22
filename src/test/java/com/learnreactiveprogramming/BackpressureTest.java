package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class BackpressureTest {

    @Test
    void testBackPressure() {
        //given
        var numberRange = Flux.range(1,100).log();

        //when

        numberRange
                .subscribe(new BaseSubscriber<>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        //super.hookOnSubscribe(subscription);
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        //super.hookOnNext(value);
                        log.info("hookOnNext: {}", value);

                        if(value == 2) {
                            cancel();
                        }
                    }

                    @Override
                    protected void hookOnComplete() {
                        //super.hookOnComplete();
                        log.info("Inside hookOnComplete");
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        //super.hookOnError(throwable);
                        log.info("Inside hookOnError");
                    }

                    @Override
                    protected void hookOnCancel() {
                        //super.hookOnCancel();
                        log.info("Inside hookOnCancel");
                    }
                });
/*                .subscribe(number -> {
                    log.info("Number is: {}", number);
                });*/

        //then


    }

    @Test
    void testBackPressure1() throws InterruptedException {
        //given
        var numberRange = Flux.range(1,100).log();
        var latch = new CountDownLatch(1);

        //when

        numberRange
                .subscribe(new BaseSubscriber<>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        //super.hookOnSubscribe(subscription);
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        //super.hookOnNext(value);
                        log.info("hookOnNext: {}", value);

                        if(value % 2 == 0  || value < 50) {
                            request(2);
                            return;
                        }
                        cancel();
                    }

                    @Override
                    protected void hookOnComplete() {
                        //super.hookOnComplete();
                        log.info("Inside hookOnComplete");
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        //super.hookOnError(throwable);
                        log.info("Inside hookOnError");
                    }

                    @Override
                    protected void hookOnCancel() {
                        //super.hookOnCancel();
                        log.info("Inside hookOnCancel");
                        latch.countDown();
                    }
                });
/*                .subscribe(number -> {
                    log.info("Number is: {}", number);
                });*/

        //then
        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }

    @Test
    void testBackPressureDrop() throws InterruptedException {
        //given
        var numberRange = Flux.range(1,100).log();
        var latch = new CountDownLatch(1);

        //when

        numberRange
                .onBackpressureDrop(item -> {
                    log.info("Dropped Items are: {}", item);
                })
                .subscribe(new BaseSubscriber<>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        //super.hookOnSubscribe(subscription);
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        //super.hookOnNext(value);
                        log.info("hookOnNext: {}", value);

                        /*if(value % 2 == 0  || value < 50) {
                            request(2);
                            return;
                        }
                        cancel();*/

                        if (value == 2) {
                            hookOnCancel();
                        }
                    }

                    @Override
                    protected void hookOnComplete() {
                        //super.hookOnComplete();
                        log.info("Inside hookOnComplete");
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        //super.hookOnError(throwable);
                        log.info("Inside hookOnError");
                    }

                    @Override
                    protected void hookOnCancel() {
                        //super.hookOnCancel();
                        log.info("Inside hookOnCancel");
                        latch.countDown();
                    }
                });
/*                .subscribe(number -> {
                    log.info("Number is: {}", number);
                });*/

        //then
        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }

    @Test
    void testBackPressureBuffer() throws InterruptedException {
        //given
        var numberRange = Flux.range(1,100).log();
        var latch = new CountDownLatch(1);

        //when

        numberRange
                .onBackpressureBuffer(10, item -> {
                    log.info("Last buffered element is: {}", item);
                })
                .subscribe(new BaseSubscriber<>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        //super.hookOnSubscribe(subscription);
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        //super.hookOnNext(value);
                        log.info("hookOnNext: {}", value);

                        /*if(value % 2 == 0  || value < 50) {
                            request(2);
                            return;
                        }
                        cancel();*/

                        if (value < 50) {
                            request(1);
                            return;
                        }
                        hookOnCancel();
                    }

                    @Override
                    protected void hookOnComplete() {
                        //super.hookOnComplete();
                        log.info("Inside hookOnComplete");
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        //super.hookOnError(throwable);
                        log.info("Inside hookOnError");
                    }

                    @Override
                    protected void hookOnCancel() {
                        //super.hookOnCancel();
                        log.info("Inside hookOnCancel");
                        latch.countDown();
                    }
                });
/*                .subscribe(number -> {
                    log.info("Number is: {}", number);
                });*/

        //then
        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }

    @Test
    void testBackPressureError() throws InterruptedException {
        //given
        var numberRange = Flux.range(1,100).log();
        var latch = new CountDownLatch(1);

        //when

        numberRange
                .onBackpressureError()
                .subscribe(new BaseSubscriber<>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        //super.hookOnSubscribe(subscription);
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        //super.hookOnNext(value);
                        log.info("hookOnNext: {}", value);

                        /*if(value % 2 == 0  || value < 50) {
                            request(2);
                            return;
                        }
                        cancel();*/

                        if (value < 50) {
                            request(1);
                            return;
                        }
                        hookOnCancel();
                    }

                    @Override
                    protected void hookOnComplete() {
                        //super.hookOnComplete();
                        log.info("Inside hookOnComplete");
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        //super.hookOnError(throwable);
                        log.error("Inside hookOnError", throwable);
                    }

                    @Override
                    protected void hookOnCancel() {
                        //super.hookOnCancel();
                        log.info("Inside hookOnCancel");
                        latch.countDown();
                    }
                });
/*                .subscribe(number -> {
                    log.info("Number is: {}", number);
                });*/

        //then
        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }
}
