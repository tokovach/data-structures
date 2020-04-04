package com.sirma.javacourse.chatserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.sirma.javacourse.chatserver.serverworkers.ActionType;
import com.sirma.javacourse.chatserver.serverworkers.ClientConnection;
import com.sirma.javacourse.chatserver.serverworkers.ClientMessageReceiver;
import com.sirma.javacourse.chatserver.serverworkers.Connection;
import com.sirma.javacourse.chatserver.serverworkers.Message;
import com.sirma.javacourse.chatserver.serverworkers.Observer;

class ClientMessageReceiverTest {
  private LinkedBlockingQueue<Message> messages;
  private LinkedBlockingQueue<String> removableUsernames;
  private LinkedBlockingQueue<String> logMessages;
  private Path regularReadPath =
      Paths.get(".\\src\\test\\resources\\receiver-test-input.txt");
  private Path noJsonReadPath =
      Paths.get(".\\src\\test\\resources\\receiver-no-json-input.txt");
  private Path malformedUsernameReadPath =
      Paths.get(".\\src\\test\\resources\\receiver-malformed-json-input.txt");
  private Path missingActionReadPath =
      Paths.get(".\\src\\test\\resources\\receiver-missing-field-json-input.txt");
  private Path disconnectReadPath =
      Paths.get(".\\src\\test\\resources\\receiver-test-leaving.txt");
  private BufferedReader reader;
  private Connection connection;
  private Connection mockedConnection;
  private Observer dummyObserver =
      new Observer() {
        @Override
        public void update(Message message) {
          logMessages.offer(message.getActionType().toString() + message.getUsername());
        }
      };

  private void initConnection(Path path) throws IOException {
    logMessages = new LinkedBlockingQueue<>();
    messages = new LinkedBlockingQueue<>(3);
    removableUsernames = new LinkedBlockingQueue<>();
    reader = Files.newBufferedReader(path);
    connection = new ClientConnection(new Socket(), reader, null);
    mockedConnection = Mockito.spy(connection);
    Mockito.doNothing().when(mockedConnection).close();
  }

  private ClientMessageReceiver initReceiver(String username) {
    return new ClientMessageReceiver(
        messages,
        username,
        mockedConnection,
        (user) -> {
          try {
            removableUsernames.put(user);
          } catch (InterruptedException e) {
            Assertions.fail(e.getMessage());
          }
        });
  }

  @Test
  void process_CheckSavedUsername() throws IOException {
    initConnection(regularReadPath);
    String username = "Test";
    ClientMessageReceiver messageReceiver = initReceiver(username);
    messageReceiver.process();
    assertEquals(username, messages.poll().getUsername());
  }

  @Test
  void process_CheckActionType() throws IOException {
    initConnection(regularReadPath);
    String username = "Test";
    ClientMessageReceiver messageReceiver = initReceiver(username);
    messageReceiver.process();
    Assertions.assertEquals(ActionType.CLIENT_MESSAGE, messages.poll().getActionType());
  }

  @Test
  void process_CheckSavedMessages() throws IOException {
    initConnection(regularReadPath);
    String username = "Test";
    String message1 = "hi";
    String message2 = "there";
    String message3 = "what";
    ClientMessageReceiver messageReceiver = initReceiver(username);
    messageReceiver.attach(dummyObserver);
    messageReceiver.process();
    assertEquals(message1, messages.poll().getClientMessage());
    assertEquals(message2, messages.poll().getClientMessage());
    assertEquals(message3, messages.poll().getClientMessage());
  }

  @Test
  void process_CheckReceiverStartedLog() throws IOException {
    initConnection(regularReadPath);
    String username = "Test";
    ClientMessageReceiver messageReceiver = initReceiver(username);
    messageReceiver.attach(dummyObserver);
    messageReceiver.process();
    assertTrue(logMessages.poll().contains(ActionType.MESSAGE_RECEIVER_START.toString()));
  }

  @Test
  void process_CheckInterruptAfterLastMessage() throws IOException {
    initConnection(regularReadPath);
    String username = "Test";
    ClientMessageReceiver messageReceiver = initReceiver(username);
    messageReceiver.attach(dummyObserver);
    messageReceiver.process();
    assertEquals(username, removableUsernames.poll());
    assertTrue(logMessages.poll().contains(username));
    assertTrue(logMessages.poll().contains(ActionType.CLIENT_INTERRUPT.toString()));
    Mockito.verify(mockedConnection, Mockito.times(1)).close();
  }

  @Test
  void process_CheckInterruptAfter_NoJsonMessage() throws IOException {
    initConnection(noJsonReadPath);
    String username = "Test";
    ClientMessageReceiver messageReceiver = initReceiver(username);
    messageReceiver.attach(dummyObserver);
    messageReceiver.process();
    assertEquals(username, removableUsernames.poll());
    assertTrue(logMessages.poll().contains(username));
    assertTrue(logMessages.poll().contains(ActionType.CLIENT_INTERRUPT.toString()));
    Mockito.verify(mockedConnection, Mockito.times(1)).close();
  }

  @Test
  void process_CheckInterruptAfter_MalformedUsername_JsonMessage() throws IOException {
    initConnection(malformedUsernameReadPath);
    String username = "Test";
    ClientMessageReceiver messageReceiver = initReceiver(username);
    messageReceiver.attach(dummyObserver);
    messageReceiver.process();
    assertEquals(username, removableUsernames.poll());
    assertTrue(logMessages.poll().contains(username));
    assertTrue(logMessages.poll().contains(ActionType.CLIENT_INTERRUPT.toString()));
    Mockito.verify(mockedConnection, Mockito.times(1)).close();
  }

  @Test
  void process_CheckInterruptAfter_MissingActionType_JsonMessage() throws IOException {
    initConnection(missingActionReadPath);
    String username = "Test";
    ClientMessageReceiver messageReceiver = initReceiver(username);
    messageReceiver.attach(dummyObserver);
    messageReceiver.process();
    assertEquals(username, removableUsernames.poll());
    assertTrue(logMessages.poll().contains(username));
    assertTrue(logMessages.poll().contains(ActionType.CLIENT_INTERRUPT.toString()));
    Mockito.verify(mockedConnection, Mockito.times(1)).close();
  }

  @Test
  void process_CheckDisconnectMessage() throws IOException {
    initConnection(disconnectReadPath);
    String username = "Test";
    ClientMessageReceiver messageReceiver = initReceiver(username);
    messageReceiver.attach(dummyObserver);
    messageReceiver.process();
    assertEquals(username, removableUsernames.poll());
    assertTrue(logMessages.poll().contains(username));
    assertTrue(logMessages.poll().contains(ActionType.CLIENT_DISCONNECT.toString()));
    Mockito.verify(mockedConnection, Mockito.times(1)).close();
  }
}
