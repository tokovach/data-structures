package com.sirma.javacourse.chatclient.clientworkers;

/** Observer interface that is used to update with input messages. */
public interface Observer {

  /**
   * Method is used to update objects based on input message.
   *
   * @param message is a message object
   */
  void update(Message message);
}
