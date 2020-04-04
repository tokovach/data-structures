package com.sirma.javacourse.chatclient.commands;

import static com.sirma.javacourse.chatclient.clientworkers.WorkerFactory.createMessageProcessor;
import static com.sirma.javacourse.chatclient.clientworkers.WorkerFactory.createMessageReceiver;
import static com.sirma.javacourse.chatclient.clientworkers.WorkerFactory.createReconnectProcessor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.Connection;
import com.sirma.javacourse.chatclient.clientworkers.ClientWorker;
import com.sirma.javacourse.chatclient.clientworkers.ConnectionObserver;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.clientworkers.Observer;
import com.sirma.javacourse.chatclient.clientworkers.UsernameObserver;
import com.sirma.javacourse.chatclient.list.ClientList;
import com.sirma.javacourse.chatclient.list.ClientListObserver;

/** A command provider that is used to execute client commands. */
public class CommandProvider {
  private AtomicBoolean isApplicationClosing;
  private Boolean autoReconnect;
  private LinkedBlockingQueue<Message> messages;
  private Consumer<Boolean> openApplication;
  private Connection clientConnection;
  private Observer logObserver;
  private Observer connectionObserver;
  private Observer listObserver;
  private Observer usernameObserver;
  private Thread receiverThread;
  private Thread messageProcessorThread;

  /**
   * Constructor is used to initialize the client connection on which the commands will be executed,
   * the log observer for the client, the client list that would be updated by commands, the open
   * application consumer and whether or not we want the client to automatically reconnect.
   *
   * @param clientConnection is connection implementation
   * @param logObserver is an observer implementation that updates the log
   * @param clientList is a client list object
   * @param openApplication is a consumer that accepts boolean
   * @param autoReconnect is a boolean
   */
  public CommandProvider(
      Connection clientConnection,
      Observer logObserver,
      ClientList clientList,
      Consumer<Boolean> openApplication,
      Boolean autoReconnect) {
    this.isApplicationClosing = new AtomicBoolean(false);
    this.autoReconnect = autoReconnect;
    this.messages = new LinkedBlockingQueue<>();
    this.clientConnection = clientConnection;
    this.openApplication = openApplication;
    initObservers(logObserver, clientList);
  }

  /**
   * Methods is used to send a client message if connection is made.
   *
   * @param clientMessage is a string message that we want to send
   */
  public void sendClientMessage(String clientMessage) {
    if (clientConnection.write() != null) {
      new SendClientMessageCommand(clientConnection, clientMessage).execute();
    }
  }

  /** Method is used to send a leave request message to server. */
  public void sendLeaveMessage() {
    if (clientConnection.write() != null) {
      new SendLeaveMessageCommand(clientConnection).execute();
    }
  }

  /**
   * Method is used to send a new username for client to the server.
   *
   * @param reason is an action type that represents the reason for the new username
   */
  public void sendNewUsername(ActionType reason) {
    if (clientConnection.trySetUsername(reason)) {
      sendUsername();
      return;
    }
    isApplicationClosing.set(true);
  }

  /** Method sends the username selected by client to the server if connection was successful. */
  public void sendUsername() {
    if (clientConnection.write() != null) {
      new SendUsernameCommand(clientConnection).execute();
    }
  }

  /** Method starts the message receiver on the client connection. */
  public void startMessageReceiver() {
    receiverThread = getWorkerThread(getMessageReceiver());
    receiverThread.start();
  }

  /** Method starts the message processor on the client connection. */
  public void startMessageProcessor() {
    messageProcessorThread = getWorkerThread(getMessageProcessor());
    messageProcessorThread.start();
  }

  /**
   * Method is used to set the flag of application closing and close the message receiver if it is
   * running.
   */
  public void stopMessageReceiver() {
    isApplicationClosing.set(true);
    if (receiverThread != null) {
      receiverThread.interrupt();
    }
  }

  /**
   * Method is used to set the flag of application closing and close the message processor if it is
   * running.
   */
  public void stopMessageProcessor() {
    isApplicationClosing.set(true);
    if (messageProcessorThread != null) {
      messageProcessorThread.interrupt();
    }
  }

  /**
   * Method attempts to reconnect the client to the server in an input time.
   *
   * @param isTryingToReconnect is an atomic boolean flag
   * @param reconnectTime is the time which the client should wait before attempting to reconnect
   */
  public void reconnect(AtomicBoolean isTryingToReconnect, int reconnectTime) {
    if (!isApplicationClosing.get()) {
      ClientWorker reconnectProcessor =
          createReconnectProcessor(isTryingToReconnect, this::tryResetConnection, reconnectTime);
      reconnectProcessor.attach(logObserver);
      getWorkerThread(reconnectProcessor).start();
    }
  }

  /**
   * Method calls the client connection's try reconnect method and if successful restarts the
   * message receiver and sends the username.
   *
   * @return a boolean of whether connection was successful
   */
  private Boolean tryResetConnection() {
    if (clientConnection.tryReconnect()) {
      startMessageReceiver();
      sendUsername();
      return true;
    }
    return false;
  }

  /**
   * Method is used to initialize the observers of the commands.
   *
   * @param logObserver is a log observer implementation
   * @param clientList is a client list object
   */
  private void initObservers(Observer logObserver, ClientList clientList) {
    this.logObserver = logObserver;
    listObserver = new ClientListObserver(clientList);
    usernameObserver = new UsernameObserver(this::sendNewUsername);
    if (autoReconnect) {
      connectionObserver =
          new ConnectionObserver((isReconnecting) -> reconnect(isReconnecting, 5000));
    }
  }

  /**
   * Method gets the message receiver client worker and attaches to it the needed observers.
   *
   * @return a client worker message receiver
   */
  private ClientWorker getMessageReceiver() {
    ClientWorker clientWorker = createMessageReceiver(clientConnection.read(), messages);
    clientWorker.attach(logObserver);
    clientWorker.attach(listObserver);
    if (autoReconnect) {
      clientWorker.attach(connectionObserver);
    }
    return clientWorker;
  }

  /**
   * Method gets the message processor client worker and attaches to it the needed observers.
   *
   * @return a client worker message processor
   */
  private ClientWorker getMessageProcessor() {
    ClientWorker clientWorker = createMessageProcessor(messages, openApplication);
    clientWorker.attach(logObserver);
    clientWorker.attach(listObserver);
    clientWorker.attach(usernameObserver);
    return clientWorker;
  }

  /**
   * Method returns a daemon thread for a client worker implementation.
   *
   * @param clientWorker is a client worker implementation
   * @return a daemon thread
   */
  private Thread getWorkerThread(ClientWorker clientWorker) {
    Thread thread = new Thread(clientWorker);
    thread.setDaemon(true);
    return thread;
  }
}
