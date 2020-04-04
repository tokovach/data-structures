package com.sirma.javacourse.chatserver;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.*;

import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatcommon.StatusField;
import com.sirma.javacourse.chatserver.servergui.ClientListModel;
import com.sirma.javacourse.chatserver.serverworkers.ActionType;
import com.sirma.javacourse.chatserver.serverworkers.Message;

class ClientListModelTest {
  private Message getMessage(String username, ActionType actionType) {
    return Message.saveMessage(username,actionType,"12.12.12").build();
  }


  @Test
  void update_AddClient() {
    String username = "test";
    StatusField statusField = new StatusField(new JTextArea());
    ClientListModel model = new ClientListModel(statusField);
    model.update(getMessage(username,ActionType.ADD_CLIENT_TO_LIST));
    assertTrue(statusField.getTextFromStatusField().contains("test"));
  }

  @Test
  void update_AddRemoveClient() {
    String username = "test";
    StatusField statusField = new StatusField(new JTextArea());
    ClientListModel model = new ClientListModel(statusField);
    model.update(getMessage(username,ActionType.ADD_CLIENT_TO_LIST));
    assertTrue(statusField.getTextFromStatusField().contains("test"));
    model.update(getMessage(username,ActionType.REMOVE_CLIENT_FROM_LIST));
    assertFalse(statusField.getTextFromStatusField().contains("test"));
  }

  @Test
  void update_AddList() {
    String username = "test";
    String username1 = "test1";
    String username2 = "test2";
    StatusField statusField = new StatusField(new JTextArea());
    ClientListModel model = new ClientListModel(statusField);
    model.update(getMessage(username,ActionType.ADD_CLIENT_TO_LIST));
    model.update(getMessage(username1,ActionType.ADD_CLIENT_TO_LIST));
    model.update(getMessage(username2,ActionType.ADD_CLIENT_TO_LIST));
    assertTrue(statusField.getTextFromStatusField().contains("test\n"));
    assertTrue(statusField.getTextFromStatusField().contains("test1"));
    assertTrue(statusField.getTextFromStatusField().contains("test2"));
  }


  @Test
  void update_AddList_RemoveOne() {
    String username = "test";
    String username1 = "test1";
    String username2 = "test2";
    StatusField statusField = new StatusField(new JTextArea());
    ClientListModel model = new ClientListModel(statusField);
    model.update(getMessage(username,ActionType.ADD_CLIENT_TO_LIST));
    model.update(getMessage(username1,ActionType.ADD_CLIENT_TO_LIST));
    model.update(getMessage(username2,ActionType.ADD_CLIENT_TO_LIST));
    model.update(getMessage(username,ActionType.REMOVE_CLIENT_FROM_LIST));
    assertFalse(statusField.getTextFromStatusField().contains("test\n"));
    assertTrue(statusField.getTextFromStatusField().contains("test1"));
    assertTrue(statusField.getTextFromStatusField().contains("test2"));
  }


  @Test
  void update_AddList_RemoveAll_OneByOne() {
    String username = "test";
    String username1 = "test1";
    String username2 = "test2";
    StatusField statusField = new StatusField(new JTextArea());
    ClientListModel model = new ClientListModel(statusField);
    model.update(getMessage(username,ActionType.ADD_CLIENT_TO_LIST));
    model.update(getMessage(username1,ActionType.ADD_CLIENT_TO_LIST));
    model.update(getMessage(username2,ActionType.ADD_CLIENT_TO_LIST));
    model.update(getMessage(username2,ActionType.REMOVE_CLIENT_FROM_LIST));
    model.update(getMessage(username,ActionType.REMOVE_CLIENT_FROM_LIST));
    model.update(getMessage(username1,ActionType.REMOVE_CLIENT_FROM_LIST));
    assertTrue(statusField.getTextFromStatusField().isEmpty());
  }


  @Test
  void update() {}
}