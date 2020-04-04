package com.sirma.javacourse.chatclient.clientworkers;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/** Client worker implementation that processes incoming messages from a queue. */
public class MessageProcessor extends ClientWorker {
  private LinkedBlockingQueue<Message> messages;
  private boolean isAppOpened = false;
  private Consumer<Boolean> openApplication;

  /**
   * Constructor is used to initialize the queue from which messages are processed and also consumer
   * interface that accepts a boolean when a welcome message is received to open the application.
   *
   * @param messages is a linked blocking queue of messages
   * @param openApplication is a consumer of booleans
   */
  public MessageProcessor(
      LinkedBlockingQueue<Message> messages, Consumer<Boolean> openApplication) {
    this.messages = messages;
    this.openApplication = openApplication;
  }

  /**
   * Method processes messages until the thread is interrupted and waits for new messages to be
   * added to the processing queue. The method processes incoming messages by notifying the attached
   * observers.
   */
  @Override
  void process() {
    Message message;
    while (!Thread.interrupted()) {
      try {
        message = messages.take();
        notifyIfWelcomeMessage(message);
        notifyObservers(message);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  /**
   * Method sends a boolean true to the open application consumer when a welcome message is
   * received.
   *
   * @param message is an incoming message
   */
  private void notifyIfWelcomeMessage(Message message) {
    if (!isAppOpened && message.getActionType() == ActionType.WELCOME_MESSAGE) {
      openApplication.accept(true);
      isAppOpened = true;
    }
  }
}
