package com.tomekl007.chapter_2;

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

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class VectorSpeedupFloat
{
    @Param({ "16348", "65536", "524288" })
    private int length;

    private float[] values;
    private float[] factors;
    private float[] results;

    @Setup(Level.Trial)
    public void setup()
    {
        values = new float[length];
        factors = new float[length];
        results = new float[length];

        final Random random = new Random();
        for (int i = 0; i < values.length; i++)
        {
            values[i] = random.nextFloat() * Float.MAX_VALUE;
            factors[i] = random.nextFloat();
            results[i] = values[i];
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void baseline()
    {
        final int len = values.length - 1;
        for (int i = 0; i < values.length; i++)
        {
            results[len - i] = (values[i] * factors[i]);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void forwardIteration()
    {
        final int len = values.length - 1;
        for (int i = 0; i < values.length; i++)
        {
            results[i] = (values[i] * factors[i]);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void backwardIteration()
    {
        final int len = values.length - 1;
        for (int i = len; i >= 0; i--)
        {
            results[i] = (values[i] * factors[i]);
        }
    }

    public static void main(String... args) throws RunnerException {

        Options opts = new OptionsBuilder()
            .include("VectorSpeedupFloat")
            .warmupIterations(1)
            .measurementIterations(1)
            .jvmArgs("-Xms2g", "-Xmx2g")
            .shouldDoGC(true)
            .forks(1)
            .build();

        new Runner(opts).run();
    }
}
