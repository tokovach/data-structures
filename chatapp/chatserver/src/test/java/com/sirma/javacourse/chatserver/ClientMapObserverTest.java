package com.sirma.javacourse.chatserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.sirma.javacourse.chatserver.clientmap.ClientMapObserver;
import com.sirma.javacourse.chatserver.clientmap.ServerClientMap;
import com.sirma.javacourse.chatserver.serverworkers.ActionType;
import com.sirma.javacourse.chatserver.serverworkers.ClientConnection;
import com.sirma.javacourse.chatserver.serverworkers.Connection;
import com.sirma.javacourse.chatserver.serverworkers.Message;
import com.sirma.javacourse.chatserver.serverworkers.Observer;
import com.sirma.javacourse.chatserver.serverworkers.ServerWorker;

class ClientMapObserverTest {
  private static Path writePathFirst = Paths.get(".\\src\\test\\resources\\test.txt");
  private static Path writePathSecond = Paths.get(".\\src\\test\\resources\\test1.txt");
  private static Path writeDummyPath= Paths.get(".\\src\\test\\resources\\test2.txt");
  private String username = "test";
  private Observer clientMapObserver;
  private Queue<String> clientMessages;
  private ServerClientMap clientMap;
  private Observer dummyObserver = message -> {};
  private ServerWorkerProvider factory =
      new ServerWorkerProvider(dummyObserver, clientMap);
  private ServerWorkerProvider mockedFactory = Mockito.spy(factory);
  private ServerWorker dummyReceiver =
      new ServerWorker() {
        @Override
        public void process() {}
      };
  private Message clientAddToList =
      Message.saveMessage(username, ActionType.ADD_CLIENT_TO_LIST, "12.12.12").build();
  private Message clientRemoveFromList =
      Message.saveMessage(username, ActionType.REMOVE_CLIENT_FROM_LIST, "12.12.12").build();

  private void initConnection(Path firstClientPath, Path secondClientPath) throws IOException {
    clientMap = new ServerClientMap();
    PrintWriter clientWriter = new PrintWriter(Files.newOutputStream(firstClientPath), true);
    PrintWriter secondClientWriter = new PrintWriter(Files.newOutputStream(secondClientPath), true);
    Connection connection = new ClientConnection(new Socket(), null, clientWriter);
    Connection secondConnection = new ClientConnection(new Socket(), null, secondClientWriter);
    clientMap.addClient(username, connection);
    clientMap.addClient("user", secondConnection);
    clientMapObserver = new ClientMapObserver(clientMap, mockedFactory);
  }

  void mockFactory() {
    Mockito.doReturn(dummyReceiver).when(mockedFactory).getClientMessageReceiver(username);
  }

  private void readMessageFromWriteFile(BufferedReader reader) throws IOException {
    String line;
    while ((line = reader.readLine()) != null) {
      clientMessages.add(line);
    }
  }

  private void addTwoPagesOfClients() throws IOException {
    PrintWriter clientWriter = new PrintWriter(Files.newOutputStream(writeDummyPath), true);
    Connection connection = new ClientConnection(new Socket(), null, clientWriter);
    for (int i =0;i< 30;i++){
      clientMap.addClient(String.valueOf(i),connection);
    }
  }

  @AfterAll
  static void deleteWriteFile() {
    try {
      Files.delete(writePathSecond);
    } catch (IOException ignored) {
    }
    try {
      Files.delete(writePathFirst);
    } catch (IOException ignored) {
    }

    try {
      Files.delete(writeDummyPath);
    } catch (IOException ignored) {
    }
  }

  @Test
  void update_ClientAddedToList_Verify_MessageReceiverStarted() throws IOException {
    initConnection(writePathFirst, writePathSecond);
    clientMap.attach(clientMapObserver);
    mockFactory();
    clientMapObserver.update(clientAddToList);
    Mockito.verify(mockedFactory, Mockito.times(1)).getClientMessageReceiver(username);
  }

  @Test
  void update_ClientAddedToList_Verify_ClientListSent_OnePage() throws IOException {
    initConnection(writePathFirst, writePathSecond);
    clientMap.attach(clientMapObserver);
    clientMessages = new LinkedList<>();
    BufferedReader clientReader = Files.newBufferedReader(writePathFirst);
    mockFactory();
    clientMapObserver.update(clientAddToList);
    Mockito.verify(mockedFactory, Mockito.times(2)).getMessageSender(any(), any());
    while (true) {
      readMessageFromWriteFile(clientReader);
      if (clientMessages.size() == 1) {
        String firstMessage = clientMessages.remove();
        assertTrue(firstMessage.contains(ActionType.CLIENT_LIST.toString()));
        JSONObject messageJSON = new JSONObject(firstMessage);
        List<Object> clients = messageJSON.getJSONArray("clientList").toList();
        assertEquals(2, clients.size());
        assertTrue(clients.contains(username));
        assertTrue(clients.contains("user"));
        return;
      }
    }
  }


  @Test
  void update_ClientAddedToList_Verify_ClientListSent_ThreePages() throws IOException {
    initConnection(writePathFirst, writePathSecond);
    addTwoPagesOfClients();
    clientMap.attach(clientMapObserver);
    clientMessages = new LinkedList<>();
    BufferedReader clientReader = Files.newBufferedReader(writePathFirst);
    mockFactory();
    clientMapObserver.update(clientAddToList);
    Mockito.verify(mockedFactory, Mockito.times(4)).getMessageSender(any(), any());
    while (true) {
      readMessageFromWriteFile(clientReader);
      if (clientMessages.size() == 3) {
        String messages = "";
        messages += clientMessages.remove();
        messages += clientMessages.remove();
        messages += clientMessages.remove();
        assertTrue(messages.contains(username));
        assertTrue(messages.contains("user"));
        assertTrue(messages.contains("1"));
        assertTrue(messages.contains("2"));
        assertTrue(messages.contains("25"));
        assertTrue(messages.contains("29"));
        return;
      }
    }
  }


  @Test
  void update_ClientRemovedFromList_VerifyRemoveMessageSent() throws IOException {
    initConnection(writePathFirst, writePathSecond);
    clientMap.attach(clientMapObserver);
    clientMessages = new LinkedList<>();
    BufferedReader clientReader = Files.newBufferedReader(writePathSecond);
    mockFactory();
    clientMapObserver.update(clientRemoveFromList);
    Mockito.verify(mockedFactory, Mockito.times(1)).getMessageSender(any(), any());
    while (true) {
      readMessageFromWriteFile(clientReader);
      if (!clientMessages.isEmpty()) {
        String message = clientMessages.remove();
        assertTrue(message.contains(ActionType.CLIENT_LEAVE_MESSAGE.toString()));
        assertTrue(message.contains(username));
        assertNull(clientMessages.poll());
        return;
      }
    }
  }

  @Test
  void update_UnknownActionType_VerifyNothingHappens() throws IOException {
    initConnection(writePathFirst, writePathSecond);
    clientMap.attach(clientMapObserver);
    mockFactory();
    Message wrongMessage =
        Message.saveMessage(username, ActionType.UNAVAILABLE_USERNAME, "12.12.12").build();
    clientMapObserver.update(wrongMessage);
    Mockito.verify(mockedFactory, Mockito.never()).getMessageSender(any(), any());
  }
}
