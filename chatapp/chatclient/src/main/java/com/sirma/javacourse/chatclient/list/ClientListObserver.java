package com.sirma.javacourse.chatclient.list;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.clientworkers.Observer;

/** An observer implementation that updates the client list. */
public class ClientListObserver implements Observer {
  private ClientList clientList;

  /**
   * Constructor is used to initialize the client list that is going to be updated.
   *
   * @param clientList is a client list object
   */
  public ClientListObserver(ClientList clientList) {
    this.clientList = clientList;
  }

  /**
   * Method checks action type of message and if it is client join, leave, list or connection
   * interruption it edits the client list.
   *
   * @param message is a message object
   */
  @Override
  public synchronized void update(Message message) {
    ActionType actionType = message.getActionType();
    if (actionType == ActionType.CLIENT_JOIN_MESSAGE) {
      clientList.add(message.getUsername());
      return;
    }
    if (actionType == ActionType.CLIENT_LEAVE_MESSAGE) {
      clientList.remove(message.getUsername());
      return;
    }
    if (actionType == ActionType.CLIENT_LIST) {
      clientList.addAll(message.getClientList());
      return;
    }
    if (actionType == ActionType.CONNECTION_INTERRUPT) {
      clientList.removeAll();
    }
  }
}
