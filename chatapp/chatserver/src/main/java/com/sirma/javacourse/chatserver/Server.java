package com.sirma.javacourse.chatserver;

import static com.sirma.javacourse.chatserver.ServerWorkerProvider.getWorkerThread;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;

import com.sirma.javacourse.chatcommon.log.Log;
import com.sirma.javacourse.chatcommon.log.LogModel;
import com.sirma.javacourse.chatserver.clientmap.ClientMapObserver;
import com.sirma.javacourse.chatserver.clientmap.ServerClientMap;
import com.sirma.javacourse.chatserver.log.ServerLogObserver;
import com.sirma.javacourse.chatserver.servergui.ClientListModel;
import com.sirma.javacourse.chatserver.serverworkers.Connection;
import com.sirma.javacourse.chatserver.serverworkers.IServerConnection;
import com.sirma.javacourse.chatserver.serverworkers.Observer;
import com.sirma.javacourse.chatserver.serverworkers.ServerConnection;

/** Server actions implementation that processes client connections and their messages. */
public class Server implements ServerActions {
  private ResourceBundle exceptions = ResourceBundle.getBundle("exception-messages");
  private Locale locale;
  private Path logFilePath;
  private IServerConnection serverConnection;
  private Thread clientAcceptor;
  private Thread messageProcessor;
  private Log serverLog;
  private ServerClientMap clientMap;
  private ServerWorkerProvider workerProvider;
  private Observer logObserver;

  /**
   * Constructor is used to initialize the language, port number and log file path.
   *
   * @param locale is a language
   * @param port is a integer number
   * @param logFilePath is a path object
   * @throws IOException if server cannot be opened on input port or log file path is invalid
   */
  public Server(Locale locale, int port, Path logFilePath) throws IOException {
    this.locale = locale;
    this.logFilePath = logFilePath;
    initLog();
    initClientMap();
    initServerWorkerProvider();
    initClientMapObserver();
    initServer(port);
  }

  /** Method opens server and starts accepting new connections. */
  @Override
  public void startServer() {
    openServer();
  }

  /**
   * Method closes server and stops accepting new connections. It also stops all existing client
   * connections.
   */
  @Override
  public void stopServer() {
    closeServer();
    stopClientConnections();
  }

  /**
   * Method sets the server log model.
   *
   * @param logModel is a log model object
   */
  @Override
  public void setServerLogModel(LogModel logModel) {
    serverLog.setLogModel(logModel);
  }

  /**
   * Method sets the list model.
   *
   * @param listModel is a client list model implementation.
   */
  @Override
  public void setClientListModel(ClientListModel listModel) {
    clientMap.attach(listModel);
  }

  /**
   * Method creates the server on input port.
   *
   * @param port is an integer port number
   * @throws IOException if port number is already used
   */
  private void initServer(int port) throws IOException {
    serverConnection = createServer(port);
  }

  /**
   * Method attempts to initialize server on input port.
   *
   * @param port is an integer port number
   * @return a server connection implementation
   * @throws IOException if port number is already used
   */
  private IServerConnection createServer(int port) throws IOException {
    try {
      return new ServerConnection(port);
    } catch (IOException e) {
      throw new IOException(String.format(exceptions.getString("unavailable.port.number"), port));
    }
  }

  /**
   * Method initializes the log.
   *
   * @throws IOException if log file path is invalid
   */
  private void initLog() throws IOException {
    serverLog = new Log(logFilePath);
    logObserver = new ServerLogObserver(serverLog, locale);
  }

  /** Method initializes the client map. */
  private void initClientMap() {
    clientMap = new ServerClientMap();
  }

  /** Method initializes the server worker provider. */
  private void initServerWorkerProvider() {
    workerProvider = new ServerWorkerProvider(logObserver, clientMap);
  }

  /** Method initializes the client map observer. */
  private void initClientMapObserver() {
    Observer mapObserver = new ClientMapObserver(clientMap, workerProvider);
    clientMap.attach(logObserver);
    clientMap.attach(mapObserver);
  }

  /** Method starts the client acceptor and message processor threads. */
  private void openServer() {
    clientAcceptor = getWorkerThread(workerProvider.getClientAcceptor(serverConnection));
    clientAcceptor.start();
    messageProcessor = getWorkerThread(workerProvider.getMessageProcessor());
    messageProcessor.start();
  }

  /**
   * Method closes the server connection and proceeds to close the message processor and client
   * acceptor threads.
   */
  private void closeServer() {
    closeServerConnection();
    if (clientAcceptor != null && messageProcessor != null) {
      clientAcceptor.interrupt();
      messageProcessor.interrupt();
    }
  }

  /** Method closes server connection. */
  private void closeServerConnection() {
    serverConnection.close();
  }

  /** Method closes all existing client connections. */
  private void stopClientConnections() {
    for (Connection connection : clientMap.getClients()) {
      connection.close();
    }
  }
}
