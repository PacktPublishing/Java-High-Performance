package com.tomekl007.chapter_5;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CompareAndSwapTest {

  private AtomicBoolean appStarted = new AtomicBoolean(false);

  @Test
  public void should_execute_operation_on_thread_that_wins_race() throws InterruptedException {
    //given
    CountDownLatch countDownLatch = new CountDownLatch(1);
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    executorService.submit(() -> {
      try {
        countDownLatch.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if ((appStarted.compareAndSet(false, true))) {
        System.out.println("starting app from thread: " + Thread.currentThread().getName());
      }
    });

    executorService.submit(() -> {
      try {
        countDownLatch.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if ((appStarted.compareAndSet(false, true))) {
        System.out.println("starting app from thread: " + Thread.currentThread().getName());
      }
    });

    //when
    countDownLatch.countDown();


    //then
    assertThat(appStarted);
  }
}
