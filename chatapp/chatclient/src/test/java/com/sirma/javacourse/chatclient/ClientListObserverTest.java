package com.sirma.javacourse.chatclient;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.list.ClientList;
import com.sirma.javacourse.chatclient.list.ClientListObserver;

class ClientListObserverTest {
  private ClientList clientList = Mockito.mock(ClientList.class);
  private List<String> users = new ArrayList<>();
  private String user = "test123";
  private Message addMessage =
      Message.saveMessage(ActionType.CLIENT_JOIN_MESSAGE).username(user).build();
  private Message removeMessage =
      Message.saveMessage(ActionType.CLIENT_LEAVE_MESSAGE).username(user).build();
  private Message removeAllMessage = Message.saveMessage(ActionType.CONNECTION_INTERRUPT).build();
  private Message addAllMessage =
      Message.saveMessage(ActionType.CLIENT_LIST).clientList(users).build();
  private Message invalidMessage =
      Message.saveMessage(ActionType.WELCOME_MESSAGE).username(user).build();

  @Test
  void update_AddMessage() {
    Mockito.doNothing().when(clientList).add(user);
    ClientListObserver observer = new ClientListObserver(clientList);
    observer.update(addMessage);
    Mockito.verify(clientList, Mockito.times(1)).add(user);
  }

  @Test
  void update_AddAllMessage() {
    Mockito.doNothing().when(clientList).addAll(users);
    ClientListObserver observer = new ClientListObserver(clientList);
    observer.update(addAllMessage);
    Mockito.verify(clientList, Mockito.times(1)).addAll(users);
  }

  @Test
  void update_RemoveMessage() {
    Mockito.doNothing().when(clientList).remove(user);
    ClientListObserver observer = new ClientListObserver(clientList);
    observer.update(removeMessage);
    Mockito.verify(clientList, Mockito.times(1)).remove(user);
  }

  @Test
  void update_RemoveAllMessage() {
    Mockito.doNothing().when(clientList).removeAll();
    ClientListObserver observer = new ClientListObserver(clientList);
    observer.update(removeAllMessage);
    Mockito.verify(clientList, Mockito.times(1)).removeAll();
  }

  @Test
  void update_InvalidMessage() {
    Mockito.doNothing().when(clientList).remove(user);
    Mockito.doNothing().when(clientList).removeAll();
    Mockito.doNothing().when(clientList).addAll(users);
    Mockito.doNothing().when(clientList).add(user);
    ClientListObserver observer = new ClientListObserver(clientList);
    observer.update(invalidMessage);
    Mockito.verify(clientList, Mockito.times(0)).remove(user);
    Mockito.verify(clientList, Mockito.times(0)).removeAll();
    Mockito.verify(clientList, Mockito.times(0)).addAll(users);
    Mockito.verify(clientList, Mockito.times(0)).add(user);
  }
}
