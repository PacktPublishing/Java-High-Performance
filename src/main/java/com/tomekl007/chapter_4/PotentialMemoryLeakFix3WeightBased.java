package com.tomekl007.chapter_4;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;

public class PotentialMemoryLeakFix3WeightBased {

  private CacheLoader<String, String> loader = new CacheLoader<String, String>() {
    @Override
    public String load(String key) {
      return key.toUpperCase();
    }
  };

  private Weigher<String, String> weighByLength = (key, value) -> value.length();

  private LoadingCache<String, String> cache = CacheBuilder.newBuilder()
      .maximumWeight(10)
      .weigher(weighByLength)
      .build(loader);


  void addToCache(String k, String v) {
    cache.put(k, v);
  }

  void getAndLoadIfAbsent(String k) {
    cache.getUnchecked(k);
  }

  public static void main(String[] args) {
    PotentialMemoryLeakFix3WeightBased potentialMemoryLeak = new PotentialMemoryLeakFix3WeightBased();
    for (int i = 0; i < 100_000_000; i++) {
      potentialMemoryLeak.getAndLoadIfAbsent(String.valueOf(i));
    }

  }
}
