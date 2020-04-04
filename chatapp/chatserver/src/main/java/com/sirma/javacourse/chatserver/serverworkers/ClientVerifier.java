package com.sirma.javacourse.chatserver.serverworkers;

import static com.sirma.javacourse.chatcommon.Clock.getCurrentTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Server worker implementation that is used to verify new client's username. */
public class ClientVerifier extends ServerWorker {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientVerifier.class);
  private Pattern USERNAME_PATTERN = Pattern.compile("[\\w]{2,8}");
  private Connection connection;
  private Predicate<String> isUsernameAvailable;
  private BiConsumer<String, Connection> processClientConnection;

  /**
   * Constructor is used to initialize connection that we are attempting to verify, as well as a
   * predicate interface for verifying client username and a bi consumer interface for processing
   * successfully verified clients.
   *
   * @param connection is a connection implementation
   * @param isUsernameAvailable is a predicate interface that accepts strings
   * @param processClientConnection is a bi consumer interface that accepts username and connection
   */
  public ClientVerifier(
      Connection connection,
      Predicate<String> isUsernameAvailable,
      BiConsumer<String, Connection> processClientConnection) {
    this.connection = connection;
    this.isUsernameAvailable = isUsernameAvailable;
    this.processClientConnection = processClientConnection;
  }

  /** Method verifies a client unless a connection error occurs. */
  @Override
  public void process() {
    try {
      verifyClient();
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Method verifies new client connection by checking provided username.
   *
   * @throws IOException if a connection occurs
   */
  private void verifyClient() throws IOException {
    BufferedReader reader = connection.read();
    String line;
    while (!Thread.interrupted()) {
      line = reader.readLine();
      if (line == null) {
        return;
      }
      processUsername(line);
    }
  }

  /**
   * Method processes incoming string message and gets username and action type from it and then
   * proceeds to check it.
   *
   * @param line is a string input message
   */
  private void processUsername(String line) {
    try {
      Message message = getMessage(line);
      String username = message.getUsername();
      notifyObservers(message);
      checkUsername(username);
    } catch (IllegalArgumentException | JSONException e) {
      LOGGER.error(e.getMessage());
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Method is used to get message object from string json input.
   *
   * @param line is a string
   * @return a message object
   * @throws IllegalArgumentException if there is an issue with the mapping of the json message
   * @throws JSONException if there is an issue with the format of the incoming message
   */
  private Message getMessage(String line) throws IllegalArgumentException, JSONException {
    JSONObject incomingMessage = new JSONObject(line);
    ActionType actionType = getActionType(incomingMessage);
    String username = getUsername(incomingMessage);
    return createMessage(username, actionType);
  }

  /**
   * Method retrieves the action type from the input json message.
   *
   * @param incomingMessage is a json object
   * @return an action type enum
   * @throws JSONException if there is an issue with format of json object
   * @throws IllegalArgumentException if no such field is stored in the input json object
   */
  private ActionType getActionType(JSONObject incomingMessage)
      throws IllegalArgumentException, JSONException {
    return ActionType.valueOf(incomingMessage.getString("actionType"));
  }

  /**
   * Method retrieves the username from the input json message.
   *
   * @param incomingMessage is a json object
   * @return a string username
   * @throws JSONException if there is an issue with format of json object
   * @throws IllegalArgumentException if no such field is stored in the input json object
   */
  private String getUsername(JSONObject incomingMessage)
      throws JSONException, IllegalArgumentException {
    return incomingMessage.getString("username");
  }

  /**
   * Method validates the input username and if it already taken or invalid, the verifier sends a
   * message to new client, otherwise it passes the client to the processor of new connections and
   * sends a welcome message.
   *
   * @param username is a string username
   */
  private void checkUsername(String username) {
    if (username.isEmpty() || !isUsernameCorrectFormat(username)) {
      sendMessage(username, ActionType.INVALID_FORMAT_USERNAME);
      return;
    }
    if (!isUsernameAvailable.test(username)) {
      sendMessage(username, ActionType.UNAVAILABLE_USERNAME);
      return;
    }
    sendMessage(username, ActionType.WELCOME_MESSAGE);
    processClientConnection.accept(username, connection);
    Thread.currentThread().interrupt();
  }

  /**
   * Method checks if input username is in correct format
   *
   * @param username is a string username
   * @return a boolean
   */
  private boolean isUsernameCorrectFormat(String username) {
    return USERNAME_PATTERN.matcher(username).matches();
  }

  /**
   * Method sends a message to new connection.
   *
   * @param username is the client's string username
   * @param actionType is an action type enum
   */
  private void sendMessage(String username, ActionType actionType) {
    connection.write().println(new JSONObject(createMessage(username, actionType)));
  }

  /**
   * Method is used to build message object.
   *
   * @param username is a string username
   * @param actionType is an action type enum
   * @return a message object
   */
  private Message createMessage(String username, ActionType actionType) {
    return Message.saveMessage(username, actionType, getCurrentTime()).build();
  }
}
