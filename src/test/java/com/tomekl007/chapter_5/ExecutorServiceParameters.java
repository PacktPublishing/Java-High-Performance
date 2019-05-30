package com.tomekl007.chapter_5;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class ExecutorServiceParameters {

  @Test
  @Ignore("saturate ")
  public void should_start_computations_and_saturate_executor() {
    //given
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    //when
    while (true) {
      executorService.submit(() -> {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {


        }
      });
    }

    //then will fail with out-of-memory because queue is saturated
  }

  @Test
  public void should_reject_task_if_queue_is_full() {
    //given
    ExecutorService executorService =
        new ThreadPoolExecutor(
            1,
            1,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadPoolExecutor.AbortPolicy()
        );

    //when
    assertThatThrownBy(() -> {
      for (int i = 0; i < 100; i++) {
        executorService.submit(() -> {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException ignored) {
          }
        });
      }
    });
  }
}
