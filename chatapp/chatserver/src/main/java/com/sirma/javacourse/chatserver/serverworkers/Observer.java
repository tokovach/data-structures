package com.sirma.javacourse.chatserver.serverworkers;

/** Observer interface that is used to update with input messages. */
public interface Observer {

  /**
   * Method is used to update objects based on input message.
   *
   * @param message is a message object
   */
  void update(Message message);
}
