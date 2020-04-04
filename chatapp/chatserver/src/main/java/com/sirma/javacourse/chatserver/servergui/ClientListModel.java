package com.sirma.javacourse.chatserver.servergui;

import com.sirma.javacourse.chatcommon.StatusField;
import com.sirma.javacourse.chatserver.serverworkers.ActionType;
import com.sirma.javacourse.chatserver.serverworkers.Message;
import com.sirma.javacourse.chatserver.serverworkers.Observer;

/**
 * Observer implementation that attaches to the client map and adds and removes usernames from a
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
  public void update(Message message) {
    String username = message.getUsername();
    ActionType actionType = message.getActionType();
    if (actionType == ActionType.ADD_CLIENT_TO_LIST) {
      addClientToList(username);
      return;
    }
    if (actionType == ActionType.REMOVE_CLIENT_FROM_LIST) {
      removeClientFromList(username);
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
