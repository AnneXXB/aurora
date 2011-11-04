package com.twitter.mesos.executor;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class TasksControllerTest {

  @Test
  public void testValidInput() {
    Map<Integer, String> result;

    // test valid input
    result = ProcessScanner.parseOutput("123 abc\n456 zxc");
    Map<Integer, String> expected = ImmutableMap.of(
        123, "abc",
        456, "zxc"
    );
    assertEquals(expected, result);

    // test valid input with extra new lines
    result = ProcessScanner.parseOutput("123 abc\n\n456 zxc");
    assertEquals(expected, result);

    // test good and bad input interleaved on separate lines
    result = ProcessScanner.parseOutput("123 abc\nbcfh 123\n456 zxc\n\n123 abc 123");
    assertEquals(expected, result);

    // test a mesos task has multiple process
    result = ProcessScanner.parseOutput("123 abc\n456 abc");
    expected = ImmutableMap.of(
        123, "abc",
        456, "abc"
    );
    assertEquals(expected, result);
  }

  @Test
  public void testInvalidInput() {
    Map<Integer, String> result;
    // test bad input
    result = ProcessScanner.parseOutput("fndslagdfjsg fndsogajn \njfiosd");
    assertEquals(0, result.size());

    // test bad input of incorrect pid and task id order
    result = ProcessScanner.parseOutput("abc 123");
    assertEquals(0, result.size());

    // test empty input
    result = ProcessScanner.parseOutput("");
    assertEquals(0, result.size());

    // test bad input with non-numeric pid
    result = ProcessScanner.parseOutput("123a tab");
    assertEquals(0, result.size());

    // test bad input with extra component
    result = ProcessScanner.parseOutput("123 abc abc");
    assertEquals(0, result.size());
  }
}
