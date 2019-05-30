package com.tomekl007.chapter_3;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class IterationAndWriteBenchmark {
  @Param({"1000", "10000", "200000"})
  private int length;

  private int[] intArray;
  private Integer[] integerArray;
  private ArrayList<Integer> arrayList;
  private LinkedList<Integer> linkedList;

  @Setup(Level.Trial)
  public void setup() {
    intArray = new int[length];
    integerArray = new Integer[length];
    arrayList = new ArrayList<>(length);
    linkedList = new LinkedList<>();

    final Random random = new Random();
    for (int i = 0; i < length; i++) {
      final int value = random.nextInt();
      intArray[i] = value;
      arrayList.add(value);
      linkedList.add(value);
    }

    Collections.shuffle(arrayList);
    Collections.shuffle(linkedList);
    integerArray = arrayList.toArray(integerArray);
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public int intArray() {
    long sum = 0;

    for (final int value : intArray) {
      sum += value;
    }

    return (int) (sum / length);
  }


  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public int integerArray() {
    long sum = 0;


    for (final int value : integerArray) {
      sum += value;
    }

    return (int) (sum / length);
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public int arrayList() {
    long sum = 0;


    for (final int value : arrayList) {
      sum += value;
    }

    return (int) (sum / length);
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public int linkedList() {
    long sum = 0;


    for (final int value : linkedList) {
      sum += value;
    }

    return (int) (sum / length);
  }

  public static void main(String... args) throws RunnerException {
    Options opts = new OptionsBuilder()
        .include("IterationAndWriteBenchmark")
        .warmupIterations(1)
        .measurementIterations(1)
        .jvmArgs("-Xms2g", "-Xmx2g")
        .shouldDoGC(true)
        .forks(1)
        .build();

    new Runner(opts).run();
  }
}
