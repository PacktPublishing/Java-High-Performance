package com.tomekl007.chapter_5;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class StreamSeqVsParallel {
  @Param({"1000", "2000000"})
  private int length;

  private ArrayList<Integer> arrayList;


  @Setup(Level.Trial)
  public void setup() {
    arrayList = new ArrayList<>(length);

    final Random random = new Random();
    for (int i = 0; i < length; i++) {
      final int value = random.nextInt();
      arrayList.add(value);

    }
    Collections.shuffle(arrayList);
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public void streamApiMap(Blackhole bh) {
    List<Integer> result = arrayList.stream()
        .map(v -> v * 20)
        .collect(Collectors.toList());
    bh.consume(result);
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public void streamParallelApiMap(Blackhole bh) {
    List<Integer> result = arrayList.parallelStream()
        .map(v -> v * 20)
        .collect(Collectors.toList());
    bh.consume(result);
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public void streamApiSort(Blackhole bh) {
    List<Integer> result = arrayList.stream()
        .sorted()
        .collect(Collectors.toList());
    bh.consume(result);
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public void streamParallelApiSort(Blackhole bh) {
    List<Integer> result = arrayList.parallelStream()
        .sorted()
        .collect(Collectors.toList());
    bh.consume(result);
  }


  public static void main(String... args) throws RunnerException {
    Options opts = new OptionsBuilder()
        .include("StreamSeqVsParallel")
        .warmupIterations(1)
        .measurementIterations(1)
        .jvmArgs("-Xms2g", "-Xmx2g")
        .shouldDoGC(true)
        .forks(1)
        .build();

    new Runner(opts).run();
  }
}

