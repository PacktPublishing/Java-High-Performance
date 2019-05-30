package com.tomekl007.chapter_1;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;


/**
 * run with java -jar target/benchmarks.jar MyBenchmark -f 1 -wi 3 -i 10
 * f - forks
 * wi - warmup iterations
 * i - iteration
 */
public class MyBenchmark {

    @Benchmark @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.MINUTES)
    public void testMethod() {
        int a = 1;
        int b = 2;
        int sum = a + b;
    }

}
