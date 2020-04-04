package com.sirma.javacourse.chatserver.serverworkers;

import java.util.List;

/**
 * A message implementation that follows the builder design interface plus the method chaining
 * technique.
 */
public class Message {
  private final String username;
  private final String time;
  private final ActionType actionType;
  private final String clientMessage;
  private final List<String> clientList;

  /**
   * Constructor uses a static inner builder class to initialize final variables.
   *
   * @param builder is the inner static builder
   */
  private Message(Builder builder) {
    this.username = builder.username;
    this.actionType = builder.actionType;
    this.time = builder.time;
    this.clientMessage = builder.clientMessage;
    this.clientList = builder.clientList;
  }

  /**
   * Method is used to get the action type of the message.
   *
   * @return an action type enum
   */
  public String getUsername() {
    return username;
  }

  /**
   * Method is used to get the username of the message sender.
   *
   * @return a string username
   */
  public ActionType getActionType() {
    return actionType;
  }

  /**
   * Method is used to get the time of message being acquired.
   *
   * @return a string time
   */
  public String getTime() {
    return time;
  }

  /**
   * Method is used to get client message if there is one.
   *
   * @return a string client message
   */
  public String getClientMessage() {
    return clientMessage;
  }

  /**
   * Method is used to get client list attached to message if there is one.
   *
   * @return a list of username strings
   */
  public List<String> getClientList() {
    return clientList;
  }

  /**
   * Method is used to save message object.
   *
   * @param actionType is an action type enum
   * @return a builder object
   */
  public static Builder saveMessage(String username, ActionType actionType, String time) {
    return new Builder(username, actionType, time);
  }

  /** Inner class builder implementation that is used to initialize the message fields. */
  public static class Builder {
    private final String username;
    private final ActionType actionType;
    private final String time;
    private String clientMessage;
    private List<String> clientList;

    /**
     * Constructor is used to initialize the necessary action type, username and time.
     *
     * @param username is a string username
     * @param actionType is an enum action type
     * @param time is a string time
     */
    public Builder(String username, ActionType actionType, String time) {
      this.username = username;
      this.actionType = actionType;
      this.time = time;
    }

    /**
     * Method that adds a client message to the builder object.
     *
     * @param clientMessage is a string client message
     * @return a builder object with client message field initialized
     */
    public Builder clientMessage(String clientMessage) {
      this.clientMessage = clientMessage;
      return this;
    }

    /**
     * Method that adds a client list to the builder object.
     *
     * @param clientList is a string list of usernames
     * @return a builder object with client list field initialized
     */
    public Builder clientList(List<String> clientList) {
      this.clientList = clientList;
      return this;
    }

    /**
     * Method is used to build message object.
     *
     * @return a new message object
     */
    public Message build() {
      return new Message(this);
    }
  }
}
