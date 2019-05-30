package com.tomekl007.chapter_4;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class PotentialMemoryLeakFix2SizeBased {

  private CacheLoader<String, String> loader = new CacheLoader<String, String>() {
    @Override
    public String load(String key) {
      return key.toUpperCase();
    }
  };

  private LoadingCache<String, String> cache = CacheBuilder.newBuilder()
      .expireAfterWrite(1, TimeUnit.MINUTES)
      .build(loader);


  void addToCache(String k, String v) {
    cache.put(k, v);
  }

  void getAndLoadIfAbsent(String k) {
    cache.getUnchecked(k);
  }

  public static void main(String[] args) {
    PotentialMemoryLeakFix2SizeBased potentialMemoryLeak = new PotentialMemoryLeakFix2SizeBased();
    for (int i = 0; i < 100_000_000; i++) {
      potentialMemoryLeak.getAndLoadIfAbsent(String.valueOf(i));
    }

  }
}
