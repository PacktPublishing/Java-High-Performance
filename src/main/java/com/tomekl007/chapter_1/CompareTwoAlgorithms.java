package com.tomekl007.chapter_1;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

public class CompareTwoAlgorithms {

  public static final int ARRAY_SIZE = 10_000;

  @State(Scope.Thread)
  public static class MyState {
    int[] toDivide = new int[ARRAY_SIZE];

    @Setup(Level.Trial)
    public void doSetup() {
      for (int i = 0; i < ARRAY_SIZE; i++) {
        toDivide[i] = i;
      }
    }
  }


  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void divideByTwo(MyState myState, Blackhole blackhole) {
    for (int i = 0; i < ARRAY_SIZE; i++) {
      blackhole.consume(myState.toDivide[i] / 2);
    }
  }

  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void divideByTwoBitShift(MyState myState, Blackhole blackhole) {
    for (int i = 0; i < ARRAY_SIZE; i++) {
      blackhole.consume(myState.toDivide[i] >> 1);
    }
  }

  public static void main(String... args) throws RunnerException {

    Options opts = new OptionsBuilder()
        .include("CompareTwoAlgorithms")
        .warmupIterations(1)
        .measurementIterations(1)
        .jvmArgs("-Xms2g", "-Xmx2g")
        .shouldDoGC(true)
        .forks(1)
        .build();

    new Runner(opts).run();
  }
}
