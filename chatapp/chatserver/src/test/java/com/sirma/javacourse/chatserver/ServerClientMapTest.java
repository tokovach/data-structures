package com.sirma.javacourse.chatserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import com.sirma.javacourse.chatserver.clientmap.ServerClientMap;
import com.sirma.javacourse.chatserver.serverworkers.ActionType;
import com.sirma.javacourse.chatserver.serverworkers.ClientConnection;
import com.sirma.javacourse.chatserver.serverworkers.Connection;
import com.sirma.javacourse.chatserver.serverworkers.Message;
import com.sirma.javacourse.chatserver.serverworkers.Observer;

class ServerClientMapTest {
  private static ServerClientMap clientMap = new ServerClientMap();
  private Connection connection = new ClientConnection(null, null, null);
  private Queue<Message> logMessages;
  private Observer observer =
      new Observer() {
        @Override
        public void update(Message message) {
          logMessages.add(message);
        }
      };

  @Test
  void addClient_CheckIfInMap() {
    ServerClientMap mockedMap = Mockito.spy(clientMap);
    mockedMap.addClient("test", connection);
    assertNotNull(mockedMap.getClient("test"));
  }

  @Test
  void addClient_CheckNotifyMethod() {
    logMessages = new LinkedList<>();
    ServerClientMap mockedMap = Mockito.spy(clientMap);
    mockedMap.attach(observer);
    mockedMap.addClient("test1", connection);
    Mockito.verify(mockedMap, Mockito.times(1)).notifyObservers(any());
    Message logMessage = logMessages.remove();
    assertEquals(ActionType.ADD_CLIENT_TO_LIST,logMessage.getActionType());
    assertEquals("test1",logMessage.getUsername());
  }

  @Test
  void removeClient_CheckIfRemovedFromMap() {
    ServerClientMap mockedMap = Mockito.spy(clientMap);
    mockedMap.addClient("test2", connection);
    mockedMap.removeClient("test2");
    assertNull(mockedMap.getClient("test2"));
  }

  @Test
  void removeClient_CheckNotifyMethod() {
    logMessages = new LinkedList<>();
    ServerClientMap mockedMap = Mockito.spy(clientMap);
    mockedMap.attach(observer);
    mockedMap.addClient("test3", connection);
    mockedMap.removeClient("test3");
    Mockito.verify(mockedMap, Mockito.times(2)).notifyObservers(any());
    logMessages.remove();
    Message logMessage = logMessages.remove();
    assertEquals(ActionType.REMOVE_CLIENT_FROM_LIST,logMessage.getActionType());
    assertEquals("test3",logMessage.getUsername());
  }

  @ParameterizedTest
  @ValueSource(strings = {"User", "user1", "USER22", "12"})
  void isUsernameAvailable_CheckInvalid(String username) {
    ServerClientMap mockedMap = Mockito.spy(clientMap);
    mockedMap.addClient("user", connection);
    mockedMap.addClient("USER1", connection);
    mockedMap.addClient("user22", connection);
    mockedMap.addClient("12", connection);
    assertFalse(mockedMap.isUsernameAvailable(username));
    Mockito.verify(mockedMap,Mockito.times(4)).notifyObservers(any());
  }
}
