package com.tomekl007.chapter_1;

import org.openjdk.jmh.annotations.Benchmark;


/**
 * run with java -jar target/benchmarks.jar SimplestBenchmark -f 1 -wi 3 -i 10
 * f - forks
 * wi - warmup iterations
 * i - iteration
 */
public class SimplestBenchmark {

  @Benchmark
  public void testMethod() {
    int a = 1;
    int b = 2;
    int sum = a + b;
  }

}
