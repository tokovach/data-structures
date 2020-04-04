package com.sirma.javacourse.chatserver.serverworkers;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/** A factory implementation that creates all server worker abstract class implementations. */
public class WorkerFactory {

  /**
   * Method creates a client acceptor server worker.
   *
   * @param serverConnection is a server connection implementation
   * @param clientSocketConsumer is a consumer interface that accepts connection implementations
   * @return a server worker
   */
  public static ServerWorker createClientAcceptor(
      IServerConnection serverConnection, Consumer<Connection> clientSocketConsumer) {
    return new ClientAcceptor(serverConnection, clientSocketConsumer);
  }

  /**
   * Method creates a client verifier server worker.
   *
   * @param connection is a client connection implementation
   * @param isUsernameAvailable is a predicate interface that accepts string username
   * @param processClientConnection is a bi consumer interface that accepts a string username and a
   *     connection implementation
   * @return a server worker
   */
  public static ServerWorker createClientVerifier(
      Connection connection,
      Predicate<String> isUsernameAvailable,
      BiConsumer<String, Connection> processClientConnection) {
    return new ClientVerifier(connection, isUsernameAvailable, processClientConnection);
  }

  /**
   * Method creates a message receiver server worker.
   *
   * @param messages is a linked blocking queue that accepts message objects
   * @param username is a string username of client
   * @param connection is the connection of the client
   * @param processClientRemoval is a consumer interface that accepts string username
   * @return a server worker
   */
  public static ServerWorker createMessageReceiver(
      LinkedBlockingQueue<Message> messages,
      String username,
      Connection connection,
      Consumer<String> processClientRemoval) {
    return new ClientMessageReceiver(messages, username, connection, processClientRemoval);
  }

  /**
   * Method creates a message processor server worker.
   *
   * @param messages is a linked blocking queue that accepts message objects
   * @param messageProcessor is a consumer interface that accepts message objects
   * @return a server worker
   */
  public static ServerWorker createMessageProcessor(
      LinkedBlockingQueue<Message> messages, Consumer<Message> messageProcessor) {
    return new IncomingMessageProcessor(messages, messageProcessor);
  }

  /**
   * Method creates a message sender server worker.
   *
   * @param message is a message object
   * @param connections is a collection of connections
   * @return a server worker
   */
  public static ServerWorker createMessageSender(
      Message message, Collection<Connection> connections) {
    return new MessageSender(message, connections);
  }
}
