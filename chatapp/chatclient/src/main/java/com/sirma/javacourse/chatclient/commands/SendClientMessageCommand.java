package com.sirma.javacourse.chatclient.commands;

import static com.sirma.javacourse.chatclient.clientworkers.WorkerFactory.createMessageSender;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.Connection;
import com.sirma.javacourse.chatclient.clientworkers.Message;

/** Command implementation that sends a client message. */
public class SendClientMessageCommand extends Command {
  private Connection connection;
  private String clientMessage;

  /**
   * Constructor is used to initialize the connection through which we send the client message as
   * well as the client message.
   *
   * @param connection is a connection implementation
   * @param clientMessage is a string message
   */
  public SendClientMessageCommand(Connection connection, String clientMessage) {
    this.connection = connection;
    this.clientMessage = clientMessage;
  }

  /** Method checks if message length is correct and proceeds to send it. */
  @Override
  public void execute() {
    int messageLength = clientMessage.length();
    if (messageLength <= 200 && messageLength > 0) {
      getWorkerThread(
              createMessageSender(connection.write(), getClientMessage(connection.getUsername())))
          .start();
    }
  }

  /**
   * Method is used to build the message object that we want to send.
   *
   * @param username is a string username
   * @return a message object
   */
  private Message getClientMessage(String username) {
    return Message.saveMessage(ActionType.CLIENT_MESSAGE)
        .username(username)
        .clientMessage(capitalizeMessage())
        .build();
  }

  /**
   * Method capitalizes the client message.
   *
   * @return a capitalized client message
   */
  private String capitalizeMessage() {
    return clientMessage.substring(0, 1).toUpperCase() + clientMessage.substring(1);
  }
}
