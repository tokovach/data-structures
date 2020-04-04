package com.sirma.javacourse.chatclient.clientworkers;

import static com.sirma.javacourse.chatcommon.Clock.getCurrentTime;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClientWorker implementation that is used to handle reconnection through a supplier of boolean.
 */
public class ReconnectProcessor extends ClientWorker {
  private static final Logger LOGGER = LoggerFactory.getLogger(ReconnectProcessor.class);
  private final int reconnectTime;
  private AtomicBoolean isTryingToReconnect;
  private Supplier<Boolean> tryReconnect;

  /**
   * Constructor is used to initialize the trying to reconnect atomic boolean that tracks whether
   * the application is already attempting to reconnect, and also initialize the try reconnecting
   * supplier that is used by client application to reset connection. The supplier supplies a
   * boolean based on whether reconnection was possible.
   *
   * @param isTryingToReconnect is an atomic boolean
   * @param tryReconnect is a supplier of boolean
   * @param reconnectTime is the interval of time that the client should wait before a connection
   *     attempt is made
   */
  public ReconnectProcessor(
      AtomicBoolean isTryingToReconnect, Supplier<Boolean> tryReconnect, int reconnectTime) {
    this.isTryingToReconnect = isTryingToReconnect;
    this.tryReconnect = tryReconnect;
    this.reconnectTime = reconnectTime;
  }

  /** Method attempts to reconnect. */
  @Override
  public void process() {
    reconnect();
  }

  /**
   * Method notifies the observers that a reconnect attempt is being made and then waits the
   * required time until attempting to connect. The process continues as long as the thread is not
   * interrupted or a connection is successful.
   */
  private void reconnect() {
    notifyObservers(
        Message.saveMessage(ActionType.RECONNECT_ATTEMPT).time(getCurrentTime()).build());
    waitToReconnect();
    if (tryReconnect.get()) {
      isTryingToReconnect.set(false);
      return;
    }
    if (!Thread.interrupted()) {
      reconnect();
    }
  }

  /**
   * Method uses the thread sleep method to wait for required time until attempting to reconnect.
   */
  private void waitToReconnect() {
    try {
      Thread.sleep(reconnectTime);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      LOGGER.error(e.getMessage());
    }
  }
}
