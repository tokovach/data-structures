package com.sirma.javacourse.chatclient.clientworkers;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/** An observer implementation that checks whether connection has been interrupted. */
public class ConnectionObserver implements Observer {
  private Consumer<AtomicBoolean> processReconnect;
  private AtomicBoolean isTryingToReconnect = new AtomicBoolean(false);

  /**
   * Constructor is used to initialize the consumer interface for the process reconnection that
   * takes atomic boolean isTryingToReconnect and should set it false once the consumer is done with
   * it.
   *
   * @param processReconnect is an atomic boolean consumer
   */
  public ConnectionObserver(Consumer<AtomicBoolean> processReconnect) {
    this.processReconnect = processReconnect;
  }

  /**
   * Method checks if action type of message input is connection interrupt and if it is and the
   * observer is not already trying to reconnect, it accepts the process reconnect interface.
   *
   * @param message is a message object
   */
  @Override
  public void update(Message message) {
    if (message.getActionType() == ActionType.CONNECTION_INTERRUPT && !isTryingToReconnect.get()) {
      isTryingToReconnect.set(true);
      processReconnect.accept(isTryingToReconnect);
    }
  }
}
