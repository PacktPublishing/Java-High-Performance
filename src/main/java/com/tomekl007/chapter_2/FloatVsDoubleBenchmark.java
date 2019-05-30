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
public class FloatVsDoubleBenchmark {
  private double aDouble;
  private double bDouble;
  private double cDouble;
  private double dDouble;
  private double eDouble;
  private double fDouble;
  private double gDouble;

  private float aFloat;
  private float bFloat;
  private float cFloat;
  private float dFloat;
  private float eFloat;
  private float fFloat;
  private float gFloat;

  @Setup(Level.Trial)
  public void setup() {
    final Random random = new Random();

    aDouble = random.nextDouble();
    bDouble = random.nextDouble();
    cDouble = random.nextDouble();
    dDouble = random.nextDouble();
    eDouble = random.nextDouble();
    fDouble = random.nextDouble();
    gDouble = random.nextDouble();

    aFloat = random.nextFloat();
    bFloat = random.nextFloat();
    cFloat = random.nextFloat();
    dFloat = random.nextFloat();
    eFloat = random.nextFloat();
    fFloat = random.nextFloat();
    gFloat = random.nextFloat();
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public double additionDouble() {

    return aDouble + bDouble + cDouble + dDouble + eDouble + fDouble + gDouble;
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public double subtractionDouble() {

    return aDouble - bDouble - cDouble - dDouble - eDouble - fDouble - gDouble;
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public double multiplicationDouble() {

    return aDouble * bDouble * cDouble * dDouble * eDouble * fDouble * gDouble;
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public double divisionDouble() {

    return aDouble / bDouble / cDouble / dDouble / eDouble / fDouble / gDouble;
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public double sqrtDouble() {

    return Math.sqrt(aDouble);
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public double additionFloat() {

    return aFloat + bFloat + cFloat + dFloat + eFloat + fFloat + gFloat;
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public double subtractionFloat() {

    return aFloat - bFloat - cFloat - dFloat - eFloat - fFloat - gFloat;
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public double multiplicationFloat() {

    return aFloat * bFloat * cFloat * dFloat * eFloat * fFloat * gFloat;
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public double divisionFloat() {

    return aFloat / bFloat / cFloat / dFloat / eFloat / fFloat / gFloat;
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public double sqrtFloat() {

    return Math.sqrt(aFloat);
  }

  public static void main(String... args) throws RunnerException {

    Options opts = new OptionsBuilder()
        .include("FloatVsDoubleBenchmark")
        .warmupIterations(1)
        .measurementIterations(1)
        .jvmArgs("-Xms2g", "-Xmx2g")
        .shouldDoGC(true)
        .forks(1)
        .build();

    new Runner(opts).run();
  }
}

