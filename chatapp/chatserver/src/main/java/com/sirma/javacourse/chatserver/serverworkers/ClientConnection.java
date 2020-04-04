package com.sirma.javacourse.chatserver.serverworkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Connection implementation that is used to store client socket, writer and reader. */
public class ClientConnection implements Connection {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientConnection.class);
  private final Socket clientSocket;
  private final BufferedReader clientReader;
  private final PrintWriter clientWriter;

  /**
   * Constructor is used to initialize the client connection by providing reader, writer and socket
   * of connection.
   *
   * @param clientSocket is a socket object
   * @param clientReader is a buffered reader
   * @param clientWriter is a print writer
   */
  public ClientConnection(
      Socket clientSocket, BufferedReader clientReader, PrintWriter clientWriter) {
    this.clientSocket = clientSocket;
    this.clientReader = clientReader;
    this.clientWriter = clientWriter;
  }

  /**
   * Method is used to get the reader of the connection
   *
   * @return a buffered reader
   */
  @Override
  public BufferedReader read() {
    return clientReader;
  }

  /**
   * Method is used to get the writer of the connection
   *
   * @return a print writer
   */
  @Override
  public PrintWriter write() {
    return clientWriter;
  }

  /** Method is used to close connection. */
  @Override
  public void close() {
    try {
      clientSocket.close();
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }
  }
}
