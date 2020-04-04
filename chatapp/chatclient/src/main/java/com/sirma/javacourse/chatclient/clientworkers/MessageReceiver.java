package com.sirma.javacourse.chatclient.clientworkers;

import static com.sirma.javacourse.chatcommon.Clock.getCurrentTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A client worker implementation that accepts messages from the server and adds them to a
 * processing queue.
 */
public class MessageReceiver extends ClientWorker {
  private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiver.class);
  private BufferedReader clientReader;
  private LinkedBlockingQueue<Message> messages;

  /**
   * Method is used to initialize the buffer reader from where we read messages and the queue where
   * we add the messages for processing.
   *
   * @param clientReader is a buffered reader
   * @param messages is linked blocking queue that holds message objects
   */
  public MessageReceiver(BufferedReader clientReader, LinkedBlockingQueue<Message> messages) {
    this.clientReader = clientReader;
    this.messages = messages;
  }

  /**
   * Method notifies about receiver starting and receives messages until a connection has stopped.
   */
  @Override
  public void process() {
    sendNotification(ActionType.MESSAGE_RECEIVER_START);
    try {
      readMessages();
    } catch (IOException e) {
      handleInterruptedConnection();
    }
  }

  /**
   * Method reads messages from reader until the thread is interrupted. If the connection is
   * interrupted it gets handled.
   *
   * @throws IOException if a connection error occurs
   */
  private void readMessages() throws IOException {
    String line;
    while (!Thread.interrupted()) {
      if ((line = clientReader.readLine()) != null) {
        processMessage(line);
      } else {
        handleInterruptedConnection();
      }
    }
  }

  /**
   * Method takes string input and builds it into a message object which is then added to the
   * processing queue.
   *
   * @param line is string line from buffered reader
   */
  private void processMessage(String line) {
    try {
      Message tempMessage = getMessage(line);
      messages.add(tempMessage);
    } catch (NullPointerException | JSONException e) {
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Method is used to build object from incoming json object.
   *
   * @param line is a string line that holds a json object
   * @return a message object
   */
  private Message getMessage(String line) {
    JSONObject incomingMessage = new JSONObject(line);
    ActionType actionType = getActionType(incomingMessage);
    String username = getUsername(incomingMessage);
    if (actionType == ActionType.CLIENT_MESSAGE) {
      return getClientMessage(username, incomingMessage);
    }
    if (actionType == ActionType.CLIENT_LIST) {
      return getClientListMessage(incomingMessage);
    }
    return constructMessage(username, actionType);
  }

  /**
   * Method is used to retrieve the action type of the json message
   *
   * @param incomingMessage is the incoming json message
   * @return an action type enum
   */
  private ActionType getActionType(JSONObject incomingMessage) {
    return ActionType.valueOf(incomingMessage.getString("actionType"));
  }

  /**
   * Method is used to retrieve the username of the json message
   *
   * @param incomingMessage is the incoming json message
   * @return a string username
   */
  private String getUsername(JSONObject incomingMessage) {
    return incomingMessage.getString("username");
  }

  /**
   * Method is used to retrieve the client message of the json message and build a message object.
   *
   * @param incomingMessage is the incoming json message
   * @return a message fom client
   */
  private Message getClientMessage(String username, JSONObject incomingMessage) {
    String clientMessage = incomingMessage.getString("clientMessage");
    return constructMessage(username, clientMessage);
  }

  /**
   * Method is used to retrieve the client list in the the json message and build a message object.
   *
   * @param incomingMessage is the incoming json message
   * @return a client list message
   */
  private Message getClientListMessage(JSONObject incomingMessage) {
    JSONArray usernames = incomingMessage.getJSONArray("clientList");
    List<String> tempUsernames = new ArrayList<>();
    for (int i = 0; i < usernames.length(); i++) {
      tempUsernames.add(String.valueOf(usernames.get(i)));
    }
    return constructMessage(tempUsernames);
  }

  /**
   * Method constructs a client list message.
   *
   * @param usernames is a list of string usernames
   * @return a message object
   */
  private Message constructMessage(List<String> usernames) {
    return Message.saveMessage(ActionType.CLIENT_LIST).clientList(usernames).build();
  }

  /**
   * Method constructs a client information message.
   *
   * @param username is a string username
   * @param actionType is an enum action type
   * @return a message object
   */
  private Message constructMessage(String username, ActionType actionType) {
    return Message.saveMessage(actionType).username(username).time(getCurrentTime()).build();
  }

  /**
   * Method constructs a client message.
   *
   * @param username is a string username
   * @param clientMessage is a string client message
   * @return a message object
   */
  private Message constructMessage(String username, String clientMessage) {
    return Message.saveMessage(ActionType.CLIENT_MESSAGE)
        .username(username)
        .clientMessage(clientMessage)
        .time(getCurrentTime())
        .build();
  }

  /**
   * Method handles interrupted connection by notifying observers and interrupting the receiver
   * thread.
   */
  private void handleInterruptedConnection() {
    sendNotification(ActionType.CONNECTION_INTERRUPT);
    Thread.currentThread().interrupt();
  }

  /**
   * Method sends a notification to observers of input action type.
   *
   * @param actionType is an enum action type
   */
  private void sendNotification(ActionType actionType) {
    Message connectionMessage = Message.saveMessage(actionType).time(getCurrentTime()).build();
    notifyObservers(connectionMessage);
  }
}
