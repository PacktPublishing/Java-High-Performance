package com.tomekl007.chapter_3;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class ImmutableMapTest {

  @Test
  public void two_threads_on_access_to_mutable_map() {
    //given
    Map<String, String> mutableMap = Collections.synchronizedMap(new HashMap<>());
    mutableMap.put("USA", "123");
    mutableMap.put("Poland", "321");

    Thread thread = new Thread(() ->
        mutableMap.put("a", "b")
        //modification is possible so every access to mutableMap must be synchronized
    );

    Thread thread2 = new Thread(() ->
        mutableMap.put("a", "b")
    );


  }

  @Test
  public void should_create_immutable_map_from_mutable_map() {
    //given
    Map<String, String> mutableMap = new HashMap<>();
    mutableMap.put("USA", "123");
    mutableMap.put("Poland", "321");

    //when
    ImmutableMap<String, String> immutableMap = ImmutableMap.copyOf(mutableMap);

    //then
    assertTrue(immutableMap.containsKey("USA"));
  }

  @Test
  public void should_create_immutable_map_for_evey_modification() {
    //given
    ImmutableMap<String, String> immutableMap = ImmutableMap.of("a", "b");

    //when wants to modify needs to create a copy
    ImmutableMap<String, String> secondMap = ImmutableMap
        .<String, String>builder()
        .putAll(immutableMap)
        .put("new", "value")
        .build();

    //then
    assertTrue(secondMap.containsKey("new"));
  }

  @Test
  public void should_throw_on_modification() {
    //given
    ImmutableMap<String, String> immutableMap = ImmutableMap.of("A", "123");

    //when then
    assertThatThrownBy(() -> immutableMap.put("k", "v"))
        .isExactlyInstanceOf(UnsupportedOperationException.class);

  }

  @Test
  public void two_threads_on_access_to_immutable_map() {
    //given
    ImmutableMap<String, String> immutableMap = ImmutableMap.of("USA", "123");


    Thread thread = new Thread(() ->
        immutableMap.get("USA")
        //modification is not possible so every access to immutableMap can be not synchronized
    );

    Thread thread2 = new Thread(() ->
        immutableMap.get("USA")
    );


  }

}
