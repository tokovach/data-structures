package com.sirma.javacourse.chatserver.serverworkers;

import static com.sirma.javacourse.chatcommon.Clock.getCurrentTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import org.json.JSONException;
import org.json.JSONObject;

/** Server worker implementation that receives particular client messages. */
public class ClientMessageReceiver extends ServerWorker {
  private LinkedBlockingQueue<Message> messages;
  private String username;
  private Connection connection;
  private Consumer<String> processClientRemoval;

  /**
   * Constructor is used to initialize the queue where messages are added, the username of
   * connection to which the receiver is associated, the connection from which we are reading and
   * the consumer interface for removing clients if connection is interrupted or they quit.
   *
   * @param messages is a linked blocking queue that works with message objects
   * @param username is the string username of the client whose connection we are reading from
   * @param connection is the connection from which we are reading
   * @param processClientRemoval is a consumer interface that accepts string usernames
   */
  public ClientMessageReceiver(
      LinkedBlockingQueue<Message> messages,
      String username,
      Connection connection,
      Consumer<String> processClientRemoval) {
    this.messages = messages;
    this.username = username;
    this.connection = connection;
    this.processClientRemoval = processClientRemoval;
  }

  /**
   * Method notifies observers of it starting and then proceeds to receive messages until a
   * connection or verification issue with json occurs.
   */
  @Override
  public void process() {
    notifyObservers(constructMessage(ActionType.MESSAGE_RECEIVER_START));
    try {
      receiveMessages();
    } catch (IOException | JSONException | NullPointerException e) {
      removeClient(ActionType.CLIENT_INTERRUPT);
    }
  }

  /**
   * Method reads from connection and checks if message is a leave request or if the receiver thread
   * has been interrupted and then proceeds to add the message to the processing queue.
   *
   * @throws IOException if a connection error occurs
   */
  private void receiveMessages() throws IOException {
    BufferedReader reader = connection.read();
    String line;
    while ((line = reader.readLine()) != null && !Thread.interrupted()) {
      Message message = getMessage(line);
      if (isClientLeaving(message)) {
        return;
      }
      if (!Thread.interrupted()) {
        messages.add(message);
      }
    }
    removeClient(ActionType.CLIENT_INTERRUPT);
  }

  /**
   * Method builds a message object from string json input.
   *
   * @param line is a string input
   * @return a message object
   */
  private Message getMessage(String line) {
    JSONObject incomingMessage = new JSONObject(line);
    ActionType actionType = getActionType(incomingMessage);
    if (incomingMessage.has("clientMessage")) {
      String clientMessage = incomingMessage.getString("clientMessage");
      return constructMessage(actionType, clientMessage);
    }
    return constructMessage(actionType);
  }

  /**
   * Method gets the action type from input json message
   *
   * @param incomingMessage is a json object
   * @return an action type enum
   */
  private ActionType getActionType(JSONObject incomingMessage) {
    return ActionType.valueOf(incomingMessage.getString("actionType"));
  }

  /**
   * Method checks if client request to leave.
   *
   * @param message is a message
   * @return a boolean
   */
  private boolean isClientLeaving(Message message) {
    if (message.getActionType().equals(ActionType.CLIENT_LEAVE_REQUEST)) {
      removeClient(ActionType.CLIENT_DISCONNECT);
      return true;
    }
    return false;
  }

  /**
   * Method passes username to consumer for client removal, closes connection, notifies observers of
   * the reason for removing client and interrupts the thread.
   *
   * @param actionType is the action type reason for client removal
   */
  private void removeClient(ActionType actionType) {
    processClientRemoval.accept(username);
    connection.close();
    notifyObservers(constructMessage(actionType));
    Thread.currentThread().interrupt();
  }

  /**
   * Method is used to build a message object based on input action type.
   *
   * @param actionType is an enum action type
   * @return a message object
   */
  private Message constructMessage(ActionType actionType) {
    return Message.saveMessage(username, actionType, getCurrentTime()).build();
  }

  /**
   * Method is used to build a client message object based on input action type.
   *
   * @param actionType is an enum action type
   * @param clientMessage is a string client message
   * @return a message object
   */
  private Message constructMessage(ActionType actionType, String clientMessage) {
    return Message.saveMessage(username, actionType, getCurrentTime())
        .clientMessage(clientMessage)
        .build();
  }
}
