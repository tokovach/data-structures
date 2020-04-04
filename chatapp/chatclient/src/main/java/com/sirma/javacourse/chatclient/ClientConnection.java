package com.sirma.javacourse.chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;

/** A connection implementation that provides a reader and writer for client connection. */
public class ClientConnection implements Connection {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientConnection.class);
  private InetSocketAddress serverAddress;
  private Function<String, String> selectUsername;
  private String username;
  private Socket clientSocket;
  private BufferedReader clientReader;
  private PrintWriter clientWriter;

  /**
   * Constructor is used to initialize the server address and the username selector.
   *
   * @param serverAddress is an inet socket address
   * @param selectUsername is a functional interface that accepts a string reason for change and
   *     returns a string username
   * @throws IOException when connection to the provided server address is not possible
   */
  public ClientConnection(InetSocketAddress serverAddress, Function<String, String> selectUsername)
      throws IOException {
    this.serverAddress = serverAddress;
    this.selectUsername = selectUsername;
    initConnection(serverAddress);
  }

  /** Method closes the connection to the server. */
  @Override
  public void close() {
    try {
      clientSocket.close();
    } catch (IOException | NullPointerException e) {
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Method gets the buffered reader for the connection to the server.
   *
   * @return a buffered reader
   */
  @Override
  public BufferedReader read() {
    return clientReader;
  }

  /**
   * Method gets the print writer for the connection to the server.
   *
   * @return a print writer
   */
  @Override
  public PrintWriter write() {
    return clientWriter;
  }

  /**
   * Method attempts to reconnect to server address input and returns whether it was successful.
   *
   * @return a boolean
   */
  @Override
  public boolean tryReconnect() {
    try {
      resetStreams();
      initConnection(serverAddress);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Method attempts to set new username due to action type reason.
   *
   * @param actionType is an enum action type
   * @return a boolean
   */
  @Override
  public boolean trySetUsername(ActionType actionType) {
    String tempUser = selectUsername.apply(actionType.toString());
    if (tempUser == null) {
      close();
      return false;
    }
    this.username = tempUser;
    return true;
  }

  /**
   * Method returns the selected username
   *
   * @return a string username
   */
  @Override
  public String getUsername() {
    return username;
  }

  /** Method is used to reset streams. */
  private void resetStreams() {
    clientReader = null;
    clientWriter = null;
  }

  /**
   * Method is used to initialize connection to server address input.
   *
   * @param serverAddress is an inet socket address
   * @throws IOException when connection to the input address is not possible
   */
  private void initConnection(InetSocketAddress serverAddress) throws IOException {
    clientSocket = new Socket();
    clientSocket.connect(serverAddress, 100);
    clientReader =
        new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
    clientWriter =
        new PrintWriter(
            new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
  }
}
