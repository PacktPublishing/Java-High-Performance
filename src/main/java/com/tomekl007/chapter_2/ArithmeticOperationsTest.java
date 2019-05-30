package com.tomekl007.chapter_2;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ArithmeticOperationsTest {
  private long a;
  private long b;
  private long c;
  private long d;
  private long e;
  private long f;
  private long g;

  @Setup(Level.Trial)
  public void setup() {
    final Random random = new Random();

    a = random.nextInt(100) + 1;
    b = random.nextInt(100) + 1;
    c = random.nextInt(100) + 1;
    d = random.nextInt(100) + 1;
    e = random.nextInt(100) + 1;
    f = random.nextInt(100) + 1;
    g = random.nextInt(100) + 1;
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public long addition() {
    return a + b + c + d + e + f + g;
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public long subtraction() {

    return a - b - c - d - e - f - g;
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public long multiplication() {
    return a * b * c * d * e * f * g;
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public long division() {
    return a / b / c / d / e / f / g;
  }

  public static void main(String... args) throws RunnerException {

    Options opts = new OptionsBuilder()
        .include("ArithmeticOperationsTest")
        .warmupIterations(1)
        .measurementIterations(1)
        .jvmArgs("-Xms2g", "-Xmx2g")
        .shouldDoGC(true)
        .forks(1)
        .build();

    new Runner(opts).run();
  }
}
