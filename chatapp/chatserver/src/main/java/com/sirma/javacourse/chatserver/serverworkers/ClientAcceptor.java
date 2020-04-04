package com.sirma.javacourse.chatserver.serverworkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Server worker implementation that accepts new client connections. */
public class ClientAcceptor extends ServerWorker {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientAcceptor.class);
  private IServerConnection serverConnection;
  private Consumer<Connection> clientVerifier;

  /**
   * Constructor is used to initialize the server connection through which we listen for new clients
   * and also the consumer client verifier which accepts new connections.
   *
   * @param serverConnection is a server connection implementation
   * @param clientVerifier is consumer interface that accepts connection implementations
   */
  public ClientAcceptor(IServerConnection serverConnection, Consumer<Connection> clientVerifier) {
    this.serverConnection = serverConnection;
    this.clientVerifier = clientVerifier;
  }

  /**
   * Method accepts new clients unless a connection error occurs which causes the acceptor to be
   * interrupted.
   */
  @Override
  public void process() {
    try {
      acceptClients();
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Method accepts new clients until the current thread is not interrupted.
   *
   * @throws IOException when a connection issue occurs
   */
  private void acceptClients() throws IOException {
    while (!Thread.interrupted()) {
      Socket clientSocket = serverConnection.accept();
      processAcceptedClient(clientSocket);
    }
  }

  /**
   * Method processes a new connection by opening buffered reader and print writer and passing them
   * as a new connection to client verifier.
   *
   * @param clientSocket is a new connection socket
   */
  private void processAcceptedClient(Socket clientSocket) {
    try {
      BufferedReader clientReader =
          new BufferedReader(
              new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
      PrintWriter clientWriter =
          new PrintWriter(
              new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
      clientVerifier.accept(new ClientConnection(clientSocket, clientReader, clientWriter));
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }
  }
}
