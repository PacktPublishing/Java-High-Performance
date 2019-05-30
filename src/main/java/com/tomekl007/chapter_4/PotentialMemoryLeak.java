package com.tomekl007.chapter_4;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class PotentialMemoryLeak {

  private CacheLoader<String, String> loader = new CacheLoader<String, String>() {
    @Override
    public String load(String key) {
      return key.toUpperCase();
    }
  };

  private LoadingCache<String, String> cache = CacheBuilder.newBuilder()
      .expireAfterWrite(120, TimeUnit.MINUTES)
      .build(loader);


  void addToCache(String k, String v) {
    cache.put(k, v);
  }

  void getAndLoadIfAbsent(String k) {
    cache.getUnchecked(k);
  }

  //1. start with VM Options: -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=myrecording.jfr
  //2. open recording file jmc -open myrecording.jfr
  //Home ./bin/jfr summary /Users/tomaszlelek/IntelliJ_workspace/jmh_packt/myrecording.jfr

  //pre-10
  //-XX:+UnlockCommercialFeatures -XX:+FlightRecorder


  public static void main(String[] args) {
    PotentialMemoryLeak potentialMemoryLeak = new PotentialMemoryLeak();
    for (int i = 0; i < 100_000_000; i++) {
      potentialMemoryLeak.getAndLoadIfAbsent(String.valueOf(i));
    }

  }
}
