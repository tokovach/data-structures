package com.sirma.javacourse.chatclient.clientgui;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.clientworkers.Observer;
import com.sirma.javacourse.chatcommon.StatusField;

/**
 * Observer implementation that attaches to the client list and adds and removes usernames from a
 * status field.
 */
public class ClientListModel implements Observer {
  private StatusField listStatusField;

  /**
   * Constructor used to initialize the status field where the client list is shown.
   *
   * @param statusField is a s wrapper object of a user interface field
   */
  public ClientListModel(StatusField statusField) {
    this.listStatusField = statusField;
  }

  /**
   * Method updates status field client list based on the the input message.
   *
   * @param message is a message object
   */
  @Override
  public synchronized void update(Message message) {
    ActionType actionType = message.getActionType();
    if (actionType == ActionType.CLIENT_JOIN_MESSAGE) {
      addClientToList(message.getUsername());
      return;
    }
    if (actionType == ActionType.CLIENT_LEAVE_MESSAGE) {
      removeClientFromList(message.getUsername());
      return;
    }
    if (actionType == ActionType.CONNECTION_INTERRUPT) {
      listStatusField.setTextToStatusField("");
      return;
    }
    if (actionType == ActionType.CLIENT_LIST) {
      for (String username : message.getClientList()) {
        addClientToList(username);
      }
    }
  }

  /**
   * Method adds input username to status field.
   *
   * @param username is string username
   */
  private void addClientToList(String username) {
    listStatusField.addNewTextToStatusField(username);
  }

  /**
   * Method removes input username from status field.
   *
   * @param username is the string username we want to remove
   */
  private void removeClientFromList(String username) {
    String clients = listStatusField.getTextFromStatusField();
    String pattern = "\n(" + username + ")(?=\\W|$)";
    String newClients = clients.replaceFirst(pattern, "");
    listStatusField.setTextToStatusField(newClients);
  }
}
