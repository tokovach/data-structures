package com.sirma.javacourse.chatclient.commands;

import static com.sirma.javacourse.chatclient.clientworkers.WorkerFactory.createMessageSender;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.Connection;
import com.sirma.javacourse.chatclient.clientworkers.Message;

/** Command implementation that sends a leave request message. */
public class SendLeaveMessageCommand extends Command {
  private Connection connection;

  /**
   * Constructor is used to initialize the connection through which we send the leave request message.
   *
   * @param connection is a connection implementation
   */
  public SendLeaveMessageCommand(Connection connection) {
    this.connection = connection;
  }

  /** Method sends the leave request message. */
  @Override
  public void execute() {
    getWorkerThread(
            createMessageSender(connection.write(), getLeaveMessage(connection.getUsername())))
        .start();
  }

  /**
   * Method is used to build the message object that we want to send.
   *
   * @param username is a string username
   * @return a message object
   */
  private Message getLeaveMessage(String username) {
    return Message.saveMessage(ActionType.CLIENT_LEAVE_REQUEST).username(username).build();
  }
}
