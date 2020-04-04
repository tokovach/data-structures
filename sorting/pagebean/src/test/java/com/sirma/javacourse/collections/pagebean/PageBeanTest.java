package com.sirma.javacourse.collections.pagebean;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class PageBeanTest {
  List<Integer> list = new ArrayList<>();

  @Test
  void next_StartList() {
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    PageBean<Integer> instance = new PageBean<>(2, list);
    String result = instance.next().toString();
    assertEquals("[1, 2]", result);
  }

  @Test
  void next_NormalWorking() {
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    list.add(5);
    list.add(6);
    PageBean<Integer> instance = new PageBean<>(2, list);
    instance.next();
    String result = instance.next().toString();
    assertEquals("[3, 4]", result);
  }

  @Test
  void next_NormalEndingFullPage() {
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    PageBean<Integer> instance = new PageBean<>(2, list);
    instance.next();
    String result = instance.next().toString();
    assertEquals("[3, 4]", result);
  }

  @Test
  void next_EndingHalfPage() {
    list.add(1);
    list.add(2);
    list.add(3);
    PageBean<Integer> instance = new PageBean<>(2, list);
    instance.next();
    String result = instance.next().toString();
    assertEquals("[3]", result);
  }

  @Test
  void next_ThrowEndOfListException() {
    list.add(1);
    list.add(2);
    list.add(3);
    PageBean<Integer> instance = new PageBean<>(2, list);
    instance.next();
    instance.next();
    assertNull(instance.next());
  }

  @Test
  void previous_NormalWorking() {
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    PageBean<Integer> instance = new PageBean<>(2, list);
    instance.next();
    instance.next();
    String result = instance.previous().toString();
    assertEquals("[1, 2]", result);
  }

  @Test
  void previous_BeforeLastPartialPage() {
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    list.add(5);
    PageBean<Integer> instance = new PageBean<>(2, list);
    instance.next();
    instance.next();
    instance.next();
    String result = instance.previous().toString();
    assertEquals("[3, 4]", result);
  }

  @Test
  void previous_ThrowBeginningOfListException() {
    list.add(1);
    list.add(2);
    list.add(3);
    PageBean<Integer> instance = new PageBean<>(2, list);
    instance.next();
    assertThrows(IndexOutOfBoundsException.class, instance::previous);
  }

  @Test
  void firstPage_Normal() {
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    list.add(5);
    PageBean<Integer> instance = new PageBean<>(2, list);
    instance.next();
    instance.next();
    instance.next();
    String result = instance.firstPage().toString();
    assertEquals("[1, 2]", result);
  }

  @Test
  void firstPage_WithoutNextInitialization() {
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    list.add(5);
    PageBean<Integer> instance = new PageBean<>(2, list);
    String result = instance.firstPage().toString();
    assertEquals("[1, 2]", result);
  }

  @Test
  void lastPage_FullPage() {
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    PageBean<Integer> instance = new PageBean<>(2, list);
    instance.next();
    String result = instance.lastPage().toString();
    assertEquals("[3, 4]", result);
  }

  @Test
  void lastPage_PartialPage() {
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    list.add(5);
    PageBean<Integer> instance = new PageBean<>(2, list);
    instance.next();
    String result = instance.lastPage().toString();
    assertEquals("[5]", result);
  }

  @Test
  void getCurrentPageNumber() {
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    list.add(5);
    PageBean<Integer> instance = new PageBean<>(2, list);
    instance.next();
    instance.next();
    int result = instance.getCurrentPageNumber();
    assertEquals(2, result);
  }
}
