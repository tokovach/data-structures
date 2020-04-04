package com.sirma.javacourse.chatclient;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.clientworkers.Observer;
import com.sirma.javacourse.chatclient.clientworkers.ReconnectProcessor;

class ReconnectProcessorTest {
  private AtomicBoolean isTryingToReconnect;
  private LinkedBlockingQueue<Boolean> tryReconnect;
  private LinkedBlockingQueue<Message> log;
  private ReconnectProcessor processor;
  private Observer observer = (message) -> log.add(message);

  @Test
  void process() {
    isTryingToReconnect = new AtomicBoolean(true);
    tryReconnect = new LinkedBlockingQueue<>();
    log = new LinkedBlockingQueue<>();
    tryReconnect.add(false);
    tryReconnect.add(true);
    processor = new ReconnectProcessor(isTryingToReconnect, () -> tryReconnect.poll(), 0);
    processor.attach(observer);
    processor.process();
    assertTrue(tryReconnect.isEmpty());
    Assertions.assertEquals(ActionType.RECONNECT_ATTEMPT, log.poll().getActionType());
    assertEquals(ActionType.RECONNECT_ATTEMPT, log.poll().getActionType());
    assertFalse(isTryingToReconnect.get());
  }
}
