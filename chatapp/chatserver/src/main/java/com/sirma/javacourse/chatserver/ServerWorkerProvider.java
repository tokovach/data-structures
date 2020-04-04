package com.sirma.javacourse.chatserver;

import static com.sirma.javacourse.chatserver.serverworkers.WorkerFactory.createClientAcceptor;
import static com.sirma.javacourse.chatserver.serverworkers.WorkerFactory.createClientVerifier;
import static com.sirma.javacourse.chatserver.serverworkers.WorkerFactory.createMessageProcessor;
import static com.sirma.javacourse.chatserver.serverworkers.WorkerFactory.createMessageReceiver;
import static com.sirma.javacourse.chatserver.serverworkers.WorkerFactory.createMessageSender;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

import com.sirma.javacourse.chatserver.clientmap.ServerClientMap;
import com.sirma.javacourse.chatserver.serverworkers.Connection;
import com.sirma.javacourse.chatserver.serverworkers.IServerConnection;
import com.sirma.javacourse.chatserver.serverworkers.Message;
import com.sirma.javacourse.chatserver.serverworkers.Observer;
import com.sirma.javacourse.chatserver.serverworkers.ServerWorker;

/** Server worker provider implementation. */
public class ServerWorkerProvider {
  private Observer logObserver;
  private LinkedBlockingQueue<Message> clientMessages;
  private ServerClientMap clientMap;

  /**
   * Constructor is used to initialize the log observer, client map and language.
   *
   * @param logObserver is an observer implementation
   * @param clientMap is a server client map
   */
  public ServerWorkerProvider(Observer logObserver, ServerClientMap clientMap) {
    this.clientMessages = new LinkedBlockingQueue<>();
    this.logObserver = logObserver;
    this.clientMap = clientMap;
  }

  /**
   * Method gets a message sender server worker and attaches to it a log observer.
   *
   * @param message is a message object
   * @param clientConnections is a collection of connections
   * @return a server worker
   */
  public ServerWorker getMessageSender(Message message, Collection<Connection> clientConnections) {
    ServerWorker serverWorker = createMessageSender(message, clientConnections);
    serverWorker.attach(logObserver);
    return serverWorker;
  }

  /**
   * Method gets a message receiver server worker and attaches to it a log observer.
   *
   * @param username is a string username of client
   * @return a server worker
   */
  public ServerWorker getClientMessageReceiver(String username) {
    ServerWorker messageReceiver =
        createMessageReceiver(
            clientMessages,
            username,
            clientMap.getClient(username),
            (e) -> clientMap.removeClient(e));
    messageReceiver.attach(logObserver);
    return messageReceiver;
  }

  /**
   * Method gets a client verifier server worker and attaches to it a log observer.
   *
   * @param clientConnection is a client connection implementation
   * @return a server worker
   */
  public ServerWorker getClientVerifier(Connection clientConnection) {
    ServerWorker clientVerifier =
        createClientVerifier(
            clientConnection,
            (username) -> clientMap.isUsernameAvailable(username),
            (username, connection) -> clientMap.addClient(username, connection));
    clientVerifier.attach(logObserver);
    return clientVerifier;
  }

  /**
   * Method gets a client acceptor server worker.
   *
   * @param serverConnection is a server connection implementation
   * @return a server worker
   */
  public ServerWorker getClientAcceptor(IServerConnection serverConnection) {
    return createClientAcceptor(
        serverConnection,
        (clientSocket) -> getWorkerThread(getClientVerifier(clientSocket)).start());
  }

  /**
   * Method gets a message processor server worker.
   *
   * @return a server worker
   */
  public ServerWorker getMessageProcessor() {
    return createMessageProcessor(
        clientMessages,
        (message) -> getWorkerThread(getMessageSender(message, clientMap.getClients())).start());
  }

  /**
   * Method returns a daemon thread with server worker input as runnable.
   *
   * @param serverWorker is a server worker implementation
   * @return a daemon thread
   */
  public static Thread getWorkerThread(ServerWorker serverWorker) {
    Thread thread = new Thread(serverWorker);
    thread.setDaemon(true);
    return thread;
  }
}
