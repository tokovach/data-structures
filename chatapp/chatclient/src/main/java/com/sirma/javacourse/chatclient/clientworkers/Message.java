package com.sirma.javacourse.chatclient.clientworkers;

import java.util.List;

/**
 * A message implementation that follows the builder design interface plus the method chaining
 * technique.
 */
public class Message {
  private final ActionType actionType;
  private final String username;
  private final String time;
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
    this.clientMessage = builder.clientMessage;
    this.time = builder.time;
    this.clientList = builder.clientList;
  }

  /**
   * Method is used to get the action type of the message.
   *
   * @return an action type enum
   */
  public ActionType getActionType() {
    return actionType;
  }

  /**
   * Method is used to get the username of the message sender.
   *
   * @return a string username
   */
  public String getUsername() {
    return username;
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
  public static Builder saveMessage(ActionType actionType) {
    return new Builder(actionType);
  }

  /** Inner class builder implementation that is used to initialize the message fields. */
  public static class Builder {
    private final ActionType actionType;
    private String username;
    private String clientMessage;
    private String time;
    private List<String> clientList;

    /**
     * Constructor is used to initialize the necessary action type.
     *
     * @param actionType is an enum action type
     */
    public Builder(ActionType actionType) {
      this.actionType = actionType;
    }

    /**
     * Method that adds a username to the builder object.
     *
     * @param username is a string username
     * @return a builder object with username field initialized
     */
    public Builder username(String username) {
      this.username = username;
      return this;
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
     * Method that adds a time to the builder object.
     *
     * @param time is a string time
     * @return a builder object with time field initialized
     */
    public Builder time(String time) {
      this.time = time;
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
