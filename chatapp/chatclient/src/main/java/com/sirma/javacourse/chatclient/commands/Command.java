package com.sirma.javacourse.chatclient.commands;

import com.sirma.javacourse.chatclient.clientworkers.ClientWorker;

/** Abstract class that is based on the command pattern. */
public abstract class Command {
  /** Method is used to execute a form of action */
  abstract void execute();

  /**
   * Method returns a daemon thread for a client worker implementation.
   *
   * @param clientWorker is a client worker implementation
   * @return a daemon thread
   */
  public Thread getWorkerThread(ClientWorker clientWorker) {
    Thread thread = new Thread(clientWorker);
    thread.setDaemon(true);
    return thread;
  }
}
