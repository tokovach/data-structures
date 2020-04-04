package com.sirma.javacourse.chatserver.serverworkers;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/** Server worker implementation that processes incoming messages. */
public class IncomingMessageProcessor extends ServerWorker {
  private static final int MAX_MESSAGE_SIZE = 200;
  private Consumer<Message> sendMessage;
  private LinkedBlockingQueue<Message> messages;

  /**
   * Constructor is used to initialize the queue from where the incoming messages are taken and the
   * consumer interface for sending messages.
   *
   * @param messages is a linked blocking queue
   * @param sendMessage is a consumer interface that accepts messages
   */
  public IncomingMessageProcessor(
      LinkedBlockingQueue<Message> messages, Consumer<Message> sendMessage) {
    this.messages = messages;
    this.sendMessage = sendMessage;
  }

  @Override
  public void process() {
    processMessages();
  }

  /**
   * Method processes new messages until the thread is interrupted and the size of client messages
   * is less than 200 characters. The thread waits for new messages to be added to the queue.
   */
  private void processMessages() {
    Message message;
    while (!Thread.interrupted()) {
      try {
        message = messages.take();
        if (message.getClientMessage().length() < MAX_MESSAGE_SIZE) {
          sendMessage.accept(message);
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
