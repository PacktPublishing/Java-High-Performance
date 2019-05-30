package com.tomekl007.chapter_1;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;


public class AvoidDeadCodeElimination {

  @Benchmark
  public void testMethod(Blackhole blackhole) {
    int a = 1;
    int b = 2;
    int sum = a + b;
    blackhole.consume(sum);
  }
}

