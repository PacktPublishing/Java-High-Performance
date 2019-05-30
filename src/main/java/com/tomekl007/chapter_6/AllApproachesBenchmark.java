package com.tomekl007.chapter_6;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class AllApproachesBenchmark {
    private static final String SRC_FILE_NAME = "/tmp/src.file";
    private static final Path SOURCE = Paths.get(SRC_FILE_NAME);
    private static final String DST_FILE_NAME = "/tmp/dst.file";
    private static final Path TARGET = Paths.get(DST_FILE_NAME);

    private static final int PAGE_SIZE = 4096;

    @Param({"4096", "409600", "40960000"})
    private
    int bytesPerOp;

    private ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(PAGE_SIZE);
    private ByteBuffer heapByteBuffer = ByteBuffer.allocate(PAGE_SIZE);

    private FileChannel srcFileChannel;

    @Setup(Level.Trial)
    public void setupBenchmark() throws Exception
    {
        if (Files.exists(SOURCE))
        {
            Files.delete(SOURCE);
        }

        final RandomAccessFile randomAccessFile = new RandomAccessFile(SRC_FILE_NAME, "rw");
        randomAccessFile.setLength(bytesPerOp);
        srcFileChannel = randomAccessFile.getChannel();

        for (int i = 0, length = bytesPerOp / PAGE_SIZE; i < length; i++)
        {
            directByteBuffer.clear();
            srcFileChannel.write(directByteBuffer);
        }
    }

    @TearDown(Level.Trial)
    public void tearDownBenchmark() throws Exception
    {
        srcFileChannel.close();

        if (Files.exists(TARGET))
        {
            Files.delete(TARGET);
        }

        if (Files.exists(SOURCE))
        {
            Files.delete(SOURCE);
        }
    }

    @Setup(Level.Invocation)
    public void setupInvocation() throws Exception
    {
        if (Files.exists(TARGET))
        {
            Files.delete(TARGET);
        }

        srcFileChannel.position(0L);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void filesCopy() throws Exception
    {
        Files.copy(SOURCE, TARGET);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void transferTo() throws Exception
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(DST_FILE_NAME, "rw");
             FileChannel dstFileChannel = randomAccessFile.getChannel())
        {

            srcFileChannel.transferTo(0, srcFileChannel.size(), dstFileChannel);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void loopWithDirectBuffer() throws Exception
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(DST_FILE_NAME, "rw");
             FileChannel dstFileChannel = randomAccessFile.getChannel())
        {
            directByteBuffer.clear();
            while( srcFileChannel.read(directByteBuffer) >=0 || directByteBuffer.position() != 0){
                directByteBuffer.flip();
                dstFileChannel.write(directByteBuffer);
                directByteBuffer.compact();
            }

        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void loopWithHeapBuffer() throws Exception
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(DST_FILE_NAME, "rw");
             FileChannel dstFileChannel = randomAccessFile.getChannel())
        {
            heapByteBuffer.clear();
            while( srcFileChannel.read(heapByteBuffer) >=0 || heapByteBuffer.position() != 0){
                heapByteBuffer.flip();
                dstFileChannel.write(heapByteBuffer);
                heapByteBuffer.compact();
            }
        }
    }

    public static void main(String... args) throws RunnerException {
        Options opts = new OptionsBuilder()
            .include("AllApproachesBenchmark")
            .warmupIterations(1)
            .measurementIterations(1)
            .jvmArgs("-Xms2g", "-Xmx2g")
            .shouldDoGC(true)
            .forks(1)
            .build();

        new Runner(opts).run();
    }
}