package com.sirma.javacourse.chatserver.serverworkers;

import java.io.IOException;
import java.net.Socket;

/**
 * Server connection interface that is used to accept new socket connections and close the server
 * socket.
 */
public interface IServerConnection {
  /**
   * Method accepts new socket connections.
   *
   * @return a socket connection
   * @throws IOException if a connection error occurs
   */
  Socket accept() throws IOException;

  /** Method closes server socket connection. */
  void close();
}
