package com.sirma.javacourse.chatclient.clientworkers;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** A factory implementation that gets all client worker abstract class implementations. */
public class WorkerFactory {

  /**
   * Method initializes a message sender client worker.
   *
   * @param clientWriter is a print writer
   * @param message is the message object we want to send
   * @return a client worker implementation
   */
  public static ClientWorker createMessageSender(PrintWriter clientWriter, Message message) {
    return new MessageSender(clientWriter, message);
  }

  /**
   * Method is used to create a message receiver client worker.
   *
   * @param clientReader is a buffered reader
   * @param messages is a linked blocking queue implementation used for storing received messages
   * @return a client worker implementation
   */
  public static ClientWorker createMessageReceiver(
      BufferedReader clientReader, LinkedBlockingQueue<Message> messages) {
    return new MessageReceiver(clientReader, messages);
  }

  /**
   * Method is used to create a message processor client worker.
   *
   * @param openApplication is a consumer interface that accepts booleans
   * @param messages is a linked blocking queue implementation used for storing and processing
   *     received messages
   * @return a client worker implementation
   */
  public static ClientWorker createMessageProcessor(
      LinkedBlockingQueue<Message> messages, Consumer<Boolean> openApplication) {
    return new MessageProcessor(messages, openApplication);
  }

  /**
   * Method is used to create a reconnect processor client worker.
   *
   * @param isTryingToReconnect is an atomic boolean flag that is used to check if client is already
   *     attempting to reconnect
   * @param tryReconnect is a supplier interface of boolean
   * @return a client worker implementation
   */
  public static ClientWorker createReconnectProcessor(
      AtomicBoolean isTryingToReconnect, Supplier<Boolean> tryReconnect, int reconnectTime) {
    return new ReconnectProcessor(isTryingToReconnect, tryReconnect, reconnectTime);
  }
}
