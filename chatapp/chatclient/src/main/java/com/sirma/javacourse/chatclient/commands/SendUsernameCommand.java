package com.sirma.javacourse.chatclient.commands;

import static com.sirma.javacourse.chatclient.clientworkers.WorkerFactory.createMessageSender;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.Connection;
import com.sirma.javacourse.chatclient.clientworkers.Message;

/** Command implementation that sends a username message. */
public class SendUsernameCommand extends Command {
  private Connection connection;

  /**
   * Constructor is used to initialize the connection through which we send the username message.
   *
   * @param connection is a connection implementation
   */
  public SendUsernameCommand(Connection connection) {
    this.connection = connection;
  }

  /** Method checks if username is initialized in connection and proceeds to send it. */
  @Override
  public void execute() {
    if (connection.getUsername() != null) {
      getWorkerThread(
              createMessageSender(connection.write(), getUsernameMessage(connection.getUsername())))
          .start();
    }
  }

  /**
   * Method is used to build the message object that we want to send.
   *
   * @param username is a string username
   * @return a message object
   */
  private Message getUsernameMessage(String username) {
    return Message.saveMessage(ActionType.SEND_USERNAME).username(username).build();
  }
}
