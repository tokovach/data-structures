package com.sirma.javacourse.chatclient;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.ConnectionObserver;
import com.sirma.javacourse.chatclient.clientworkers.Message;

class ConnectionObserverTest {
  private LinkedBlockingQueue<AtomicBoolean> reconnectQueue;
  private ConnectionObserver observer;
  private Message interruptMessage = Message.saveMessage(ActionType.CONNECTION_INTERRUPT).build();

  private void addToQueue(AtomicBoolean isTryingToReconnect) {
    reconnectQueue.add(isTryingToReconnect);
  }

  @Test
  void update_Once() throws InterruptedException {
    reconnectQueue = new LinkedBlockingQueue<>();
    observer = new ConnectionObserver(this::addToQueue);
    observer.update(interruptMessage);
    AtomicBoolean isTryingToReconnect = reconnectQueue.take();
    assertTrue(isTryingToReconnect.get());
  }

  @Test
  void update_TrySecond() throws InterruptedException {
    reconnectQueue = new LinkedBlockingQueue<>();
    observer = new ConnectionObserver(this::addToQueue);
    observer.update(interruptMessage);
    AtomicBoolean isTryingToReconnect = reconnectQueue.take();
    assertTrue(isTryingToReconnect.get());
    observer.update(interruptMessage);
    assertNull(reconnectQueue.poll());
  }
}
