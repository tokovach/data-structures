package com.sirma.javacourse.chatclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.clientworkers.Observer;
import com.sirma.javacourse.chatclient.list.ClientList;

class ClientListTest {
  private LinkedBlockingQueue<Message> logMessages;
  private ClientList clientList;
  private Observer dummyObserver = message -> logMessages.offer(message);

  @Test
  void add_normal_CheckLog() {
    logMessages = new LinkedBlockingQueue<>();
    clientList = new ClientList();
    clientList.attach(dummyObserver);
    clientList.add("test");
    assertTrue(clientList.contains("test"));
    Message logMessage = logMessages.poll();
    assertEquals(logMessage.getUsername(), "test");
    assertEquals(logMessage.getActionType(), ActionType.CLIENT_JOIN_MESSAGE);
  }

  @Test
  void add_alreadyInList_CheckNotLogged() {
    logMessages = new LinkedBlockingQueue<>();
    clientList = new ClientList();
    clientList.add("test");
    clientList.attach(dummyObserver);
    clientList.add("test");
    assertNull(logMessages.poll());
  }

  @Test
  void remove_normal_CheckLog() {
    logMessages = new LinkedBlockingQueue<>();
    clientList = new ClientList();
    clientList.add("test");
    clientList.attach(dummyObserver);
    clientList.remove("test");
    assertFalse(clientList.contains("test"));
    Message logMessage = logMessages.poll();
    assertEquals(logMessage.getUsername(), "test");
    assertEquals(logMessage.getActionType(), ActionType.CLIENT_LEAVE_MESSAGE);
  }

  @Test
  void remove_alreadyRemoved_CheckNotLogged() {
    logMessages = new LinkedBlockingQueue<>();
    clientList = new ClientList();
    clientList.add("test");
    clientList.remove("test");
    clientList.attach(dummyObserver);
    clientList.remove("test");
    assertFalse(clientList.contains("test"));
    assertNull(logMessages.poll());
  }

  @Test
  void addAll_normal_CheckLog() {
    List<String> clientsToBeAdded = new ArrayList<>();
    clientsToBeAdded.add("test1");
    clientsToBeAdded.add("test2");
    clientsToBeAdded.add("test3");
    logMessages = new LinkedBlockingQueue<>();
    clientList = new ClientList();
    clientList.attach(dummyObserver);
    clientList.addAll(clientsToBeAdded);
    assertTrue(clientList.contains("test1"));
    assertTrue(clientList.contains("test2"));
    assertTrue(clientList.contains("test3"));
    Message logMessage = logMessages.poll();
    assertEquals(logMessage.getClientList(), clientsToBeAdded);
    assertEquals(logMessage.getActionType(), ActionType.CLIENT_LIST);
  }

  @Test
  void addAll_partially_CheckLog() {
    List<String> clientsToBeAdded = new ArrayList<>();
    clientsToBeAdded.add("test1");
    clientsToBeAdded.add("test2");
    clientsToBeAdded.add("test3");
    logMessages = new LinkedBlockingQueue<>();
    clientList = new ClientList();
    clientList.add("test2");
    clientList.attach(dummyObserver);
    clientList.addAll(clientsToBeAdded);
    Message logMessage = logMessages.poll();
    assertFalse(logMessage.getClientList().contains("test2"));
  }

  @Test
  void addAll_empty_CheckLog() {
    List<String> clientsToBeAdded = new ArrayList<>();
    logMessages = new LinkedBlockingQueue<>();
    clientList = new ClientList();
    clientList.attach(dummyObserver);
    clientList.addAll(clientsToBeAdded);
    assertNull(logMessages.poll());
  }

  @Test
  void removeAll() {
    List<String> clientsToBeAdded = new ArrayList<>();
    clientsToBeAdded.add("test1");
    clientsToBeAdded.add("test2");
    clientsToBeAdded.add("test3");
    logMessages = new LinkedBlockingQueue<>();
    clientList = new ClientList();
    clientList.addAll(clientsToBeAdded);
    clientList.attach(dummyObserver);
    clientList.removeAll();
    assertFalse(clientList.contains("test1"));
    Message logMessage = logMessages.poll();
    assertEquals(logMessage.getActionType(), ActionType.CONNECTION_INTERRUPT);
  }
}
