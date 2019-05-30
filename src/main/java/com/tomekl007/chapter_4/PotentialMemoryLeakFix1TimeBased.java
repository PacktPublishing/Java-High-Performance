package com.tomekl007.chapter_4;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class PotentialMemoryLeakFix1TimeBased {

  private CacheLoader<String, String> loader = new CacheLoader<String, String>() {
    @Override
    public String load(String key) {
      return key.toUpperCase();
    }
  };

  private LoadingCache<String, String> cache = CacheBuilder.newBuilder()
      .maximumSize(1000)
      .build(loader);


  void addToCache(String k, String v) {
    cache.put(k, v);
  }

  void getAndLoadIfAbsent(String k) {
    cache.getUnchecked(k);
  }

  public static void main(String[] args) {
    PotentialMemoryLeakFix1TimeBased potentialMemoryLeak = new PotentialMemoryLeakFix1TimeBased();
    for (int i = 0; i < 100_000_000; i++) {
      potentialMemoryLeak.getAndLoadIfAbsent(String.valueOf(i));
    }

  }
}
