package com.sirma.javacourse.chatserver.serverworkers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Server connection implementation that accepts connections from clients. */
public class ServerConnection implements IServerConnection {
  private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnection.class);
  private ServerSocket serverSocket;

  /**
   * Constructor is used to initialize the server socket on an input port number.
   *
   * @param port is an int port number
   * @throws IOException if server socket cannot be initialized
   */
  public ServerConnection(int port) throws IOException {
    this.serverSocket = new ServerSocket(port, 0, InetAddress.getLocalHost());
  }

  /**
   * Method accepts new socket connections.
   *
   * @return a socket connection
   * @throws IOException if a connection error occurs
   */
  @Override
  public Socket accept() throws IOException {
    return serverSocket.accept();
  }

  /** Method closes server socket connection. */
  @Override
  public void close() {
    if (serverSocket != null) {
      try {
        serverSocket.close();
      } catch (IOException e) {
        LOGGER.error(e.getMessage());
      }
    }
  }
}
