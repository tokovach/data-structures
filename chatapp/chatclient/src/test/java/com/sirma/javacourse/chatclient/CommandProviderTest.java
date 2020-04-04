package com.sirma.javacourse.chatclient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.clientworkers.Observer;
import com.sirma.javacourse.chatclient.commands.CommandProvider;
import com.sirma.javacourse.chatclient.list.ClientList;

class CommandProviderTest {
  private LinkedBlockingQueue<Message> logMessages;
  private Connection clientConnection = Mockito.mock(ClientConnection.class);
  private ClientList mockedList = Mockito.mock(ClientList.class);
  private Observer dummyLogObserver = message -> logMessages.offer(message);
  private CommandProvider provider;
  private PrintWriter clientWriter;
  private static Path writePathClient =
      Paths.get(".\\src\\test\\resources\\output-client.txt");
  private static Path writePathLeave =
      Paths.get(".\\src\\test\\resources\\output-leave.txt");
  private static Path writePathUser =
      Paths.get(".\\src\\test\\resources\\output-username.txt");

  private CommandProvider initProvider(Boolean autoReconnect) {
    return new CommandProvider(
        clientConnection, dummyLogObserver, mockedList, (b) -> {}, autoReconnect);
  }

  private String readFromFile(Path path) throws IOException {
    BufferedReader reader = Files.newBufferedReader(path);
    String line;
    String message = "";
    while ((line = reader.readLine()) != null) {
      message += line;
    }
    return message;
  }

  @AfterAll
  static void deleteWriteFile() {
    try {
      Files.delete(writePathClient);
    } catch (IOException ignored) {
    }
    try {
      Files.delete(writePathLeave);
    } catch (IOException ignored) {
    }
    try {
      Files.delete(writePathUser);
    } catch (IOException ignored) {
    }
  }

  @Test
  void sendClientMessage_Check_NotInitializedConnection() {
    logMessages = new LinkedBlockingQueue<>();
    Mockito.when(clientConnection.write()).thenReturn(null);
    provider = initProvider(false);
    provider.sendClientMessage("test");
    Mockito.verify(clientConnection, Mockito.times(1)).write();
  }

  @Test
  void sendClientMessage_Check_MessageSent() throws IOException {
    String expectedMessage = "Test";
    String username = "123";
    logMessages = new LinkedBlockingQueue<>();
    clientWriter = new PrintWriter(Files.newOutputStream(writePathClient), true);
    Mockito.when(clientConnection.write()).thenReturn(clientWriter);
    Mockito.when(clientConnection.getUsername()).thenReturn(username);
    provider = initProvider(false);
    provider.sendClientMessage("test");
    Mockito.verify(clientConnection, Mockito.times(2)).write();
    while (true) {
      String message = readFromFile(writePathClient);
      if (!message.isEmpty()) {
        assertTrue(message.contains(expectedMessage));
        assertTrue(message.contains(username));
        return;
      }
    }
  }

  @Test
  void sendLeaveMessage_Check_NotInitializedConnection() {
    logMessages = new LinkedBlockingQueue<>();
    Mockito.when(clientConnection.write()).thenReturn(null);
    provider = initProvider(false);
    provider.sendLeaveMessage();
    Mockito.verify(clientConnection, Mockito.times(1)).write();
  }

  @Test
  void sendLeaveMessage_Check_MessageSent() throws IOException {
    String username = "123";
    logMessages = new LinkedBlockingQueue<>();
    clientWriter = new PrintWriter(Files.newOutputStream(writePathLeave), true);
    Mockito.when(clientConnection.write()).thenReturn(clientWriter);
    Mockito.when(clientConnection.getUsername()).thenReturn(username);
    provider = initProvider(false);
    provider.sendLeaveMessage();
    Mockito.verify(clientConnection, Mockito.times(2)).write();
    while (true) {
      String message = readFromFile(writePathLeave);
      if (!message.isEmpty()) {
        assertTrue(message.contains(ActionType.CLIENT_LEAVE_REQUEST.toString()));
        assertTrue(message.contains(username));
        return;
      }
    }
  }

  @Test
  void sendUsername_Check_NotInitializedConnection() {
    logMessages = new LinkedBlockingQueue<>();
    Mockito.when(clientConnection.write()).thenReturn(null);
    provider = initProvider(false);
    provider.sendUsername();
    Mockito.verify(clientConnection, Mockito.times(1)).write();
  }

  @Test
  void sendUsername_Check_MessageSent() throws IOException {
    String username = "123";
    logMessages = new LinkedBlockingQueue<>();
    clientWriter = new PrintWriter(Files.newOutputStream(writePathUser), true);
    Mockito.when(clientConnection.write()).thenReturn(clientWriter);
    Mockito.when(clientConnection.getUsername()).thenReturn(username);
    provider = initProvider(false);
    provider.sendUsername();
    Mockito.verify(clientConnection, Mockito.times(2)).write();
    while (true) {
      String message = readFromFile(writePathUser);
      if (!message.isEmpty()) {
        assertTrue(message.contains(ActionType.SEND_USERNAME.toString()));
        assertTrue(message.contains(username));
        return;
      }
    }
  }

  @Test
  void reconnect_Check_NotInitializedConnection() throws InterruptedException {
    AtomicBoolean isTryingToReconnect = new AtomicBoolean(true);
    logMessages = new LinkedBlockingQueue<>();
    Mockito.when(clientConnection.tryReconnect()).thenReturn(false);
    provider = initProvider(true);
    CommandProvider mockedProvider = Mockito.spy(provider);
    Mockito.doNothing().when(mockedProvider).startMessageReceiver();
    Mockito.doNothing().when(mockedProvider).sendUsername();
    mockedProvider.reconnect(isTryingToReconnect,0);
    Thread.sleep(200);
    Mockito.verify(mockedProvider, Mockito.times(0)).startMessageReceiver();
    Mockito.verify(mockedProvider, Mockito.times(0)).sendUsername();
    assertTrue(isTryingToReconnect.get());
  }

  @Test
  void reconnect_Check_InitializedConnection() throws InterruptedException {
    AtomicBoolean isTryingToReconnect = new AtomicBoolean(true);
    logMessages = new LinkedBlockingQueue<>();
    Mockito.when(clientConnection.tryReconnect()).thenReturn(true);
    provider = initProvider(true);
    CommandProvider mockedProvider = Mockito.spy(provider);
    Mockito.doNothing().when(mockedProvider).startMessageReceiver();
    Mockito.doNothing().when(mockedProvider).sendUsername();
    mockedProvider.reconnect(isTryingToReconnect,1);
    Thread.sleep(200);
    Mockito.verify(mockedProvider, Mockito.times(1)).startMessageReceiver();
    Mockito.verify(mockedProvider, Mockito.times(1)).sendUsername();
    assertFalse(isTryingToReconnect.get());
  }

  @Test
  void sendNewUsername_Check_Normal() {
    Mockito.doReturn(true).when(clientConnection).trySetUsername(any());
    provider = initProvider(true);
    CommandProvider mockedProvider = Mockito.spy(provider);
    Mockito.doNothing().when(mockedProvider).sendUsername();
    mockedProvider.sendNewUsername(ActionType.INVALID_FORMAT_USERNAME);
    Mockito.verify(mockedProvider, Mockito.times(1)).sendUsername();
    Mockito.verify(clientConnection, Mockito.times(1))
        .trySetUsername(ActionType.INVALID_FORMAT_USERNAME);
  }

  @Test
  void sendNewUsername_Check_NotAbleToSetUsername() {
    Mockito.doReturn(false).when(clientConnection).trySetUsername(any());
    provider = initProvider(true);
    CommandProvider mockedProvider = Mockito.spy(provider);
    Mockito.doNothing().when(mockedProvider).sendUsername();
    mockedProvider.sendNewUsername(ActionType.INVALID_FORMAT_USERNAME);
    Mockito.verify(mockedProvider, Mockito.times(0)).sendUsername();
    Mockito.verify(clientConnection, Mockito.times(1))
            .trySetUsername(ActionType.INVALID_FORMAT_USERNAME);
  }
}
