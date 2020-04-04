package com.sirma.javacourse.chatclient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatclient.clientgui.ClientListModel;
import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatcommon.StatusField;

class ClientListModelTest {
  private List<String> users;

  private Message getMessage(String username, ActionType actionType) {
    return Message.saveMessage(actionType).username(username).build();
  }

  private Message getListMessage() {
    return Message.saveMessage(ActionType.CLIENT_LIST).clientList(users).build();
  }

  private Message getInterruptMessage() {
    return Message.saveMessage(ActionType.CONNECTION_INTERRUPT).build();
  }

  @Test
  void update_AddClient() {
    String username = "test";
    StatusField statusField = new StatusField(new JTextArea());
    ClientListModel model = new ClientListModel(statusField);
    model.update(getMessage(username, ActionType.CLIENT_JOIN_MESSAGE));
    assertTrue(statusField.getTextFromStatusField().contains("test"));
  }

  @Test
  void update_AddRemoveClient() {
    String username = "test";
    StatusField statusField = new StatusField(new JTextArea());
    ClientListModel model = new ClientListModel(statusField);
    model.update(getMessage(username, ActionType.CLIENT_JOIN_MESSAGE));
    assertTrue(statusField.getTextFromStatusField().contains("test"));
    model.update(getMessage(username, ActionType.CLIENT_LEAVE_MESSAGE));
    assertFalse(statusField.getTextFromStatusField().contains("test"));
  }

  @Test
  void update_AddList() {
    users = new ArrayList<>();
    String username = "test";
    String username1 = "test1";
    String username2 = "test2";
    users.add(username);
    users.add(username1);
    users.add(username2);
    StatusField statusField = new StatusField(new JTextArea());
    ClientListModel model = new ClientListModel(statusField);
    model.update(getListMessage());
    assertTrue(statusField.getTextFromStatusField().contains("test\n"));
    assertTrue(statusField.getTextFromStatusField().contains("test1"));
    assertTrue(statusField.getTextFromStatusField().contains("test2"));
  }

  @Test
  void update_AddList_RemoveOne() {
    users = new ArrayList<>();
    String username = "test";
    String username1 = "test1";
    String username2 = "test2";
    users.add(username);
    users.add(username1);
    users.add(username2);
    StatusField statusField = new StatusField(new JTextArea());
    ClientListModel model = new ClientListModel(statusField);
    model.update(getListMessage());
    model.update(getMessage(username, ActionType.CLIENT_LEAVE_MESSAGE));
    assertFalse(statusField.getTextFromStatusField().contains("test\n"));
    assertTrue(statusField.getTextFromStatusField().contains("test1"));
    assertTrue(statusField.getTextFromStatusField().contains("test2"));
  }

  @Test
  void update_AddList_RemoveAll_OneByOne() {
    users = new ArrayList<>();
    String username = "test";
    String username1 = "test1";
    String username2 = "test2";
    users.add(username);
    users.add(username1);
    users.add(username2);
    StatusField statusField = new StatusField(new JTextArea());
    ClientListModel model = new ClientListModel(statusField);
    model.update(getListMessage());
    model.update(getMessage(username2, ActionType.CLIENT_LEAVE_MESSAGE));
    model.update(getMessage(username, ActionType.CLIENT_LEAVE_MESSAGE));
    model.update(getMessage(username1, ActionType.CLIENT_LEAVE_MESSAGE));
    assertTrue(statusField.getTextFromStatusField().isEmpty());
  }

  @Test
  void update_AddList_RemoveAll_ConnectionInterrupt() {
    users = new ArrayList<>();
    String username = "test";
    String username1 = "test1";
    String username2 = "test2";
    users.add(username);
    users.add(username1);
    users.add(username2);
    StatusField statusField = new StatusField(new JTextArea());
    ClientListModel model = new ClientListModel(statusField);
    model.update(getListMessage());
    model.update(getInterruptMessage());
    assertTrue(statusField.getTextFromStatusField().isEmpty());
  }
}
