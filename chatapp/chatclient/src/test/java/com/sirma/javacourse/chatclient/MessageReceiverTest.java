package com.sirma.javacourse.chatclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.clientworkers.MessageReceiver;
import com.sirma.javacourse.chatclient.clientworkers.Observer;

class MessageReceiverTest {
  private LinkedBlockingQueue<Message> logMessages;
  private LinkedBlockingQueue<Message> clientMessages;
  private Path regularReadPath =
      Paths.get(".\\src\\test\\resources\\receiver-test-input.txt");
  private Path noJsonReadPath =
      Paths.get(".\\src\\test\\resources\\receiver-no-json-input.txt");
  private Path noJsonFirstReadPath =
      Paths.get(".\\src\\test\\resources\\receiver-no-first-json-input.txt");
  private Path malformedUsernameFirstReadPath =
      Paths.get(".\\src\\test\\resources\\receiver-malformed-json-input.txt");
  private Path missingActionReadPath =
      Paths.get(".\\src\\test\\resources\\receiver-missing-field-json-input.txt");
  private BufferedReader reader;
  private Observer dummyObserver = message -> logMessages.offer(message);

  private void initConnection(Path path) throws IOException {
    logMessages = new LinkedBlockingQueue<>();
    clientMessages = new LinkedBlockingQueue<>(3);
    reader = Files.newBufferedReader(path);
  }

  private MessageReceiver initReceiver() {
    return new MessageReceiver(reader, clientMessages);
  }

  @Test
  void processMessages_ClientMessage() throws IOException {
    initConnection(regularReadPath);
    MessageReceiver receiver = initReceiver();
    receiver.attach(dummyObserver);
    receiver.process();
    Message clientMessage = clientMessages.remove();
    Message logMessage = logMessages.remove();
    Assertions.assertEquals(clientMessage.getActionType(), ActionType.CLIENT_MESSAGE);
    assertEquals(clientMessage.getUsername(), "Test");
    assertEquals(clientMessage.getClientMessage(), "hi");
    assertEquals(logMessage.getActionType(), ActionType.MESSAGE_RECEIVER_START);
  }

  @Test
  void processMessages_InfoMessage() throws IOException {
    initConnection(regularReadPath);
    MessageReceiver receiver = initReceiver();
    receiver.process();
    clientMessages.remove();
    Message clientMessage = clientMessages.remove();
    assertEquals(clientMessage.getActionType(), ActionType.CLIENT_JOIN_MESSAGE);
    assertEquals(clientMessage.getUsername(), "Test1");
    assertNull(clientMessage.getClientMessage());
  }

  @Test
  void processMessages_ClientListMessage() throws IOException {
    initConnection(regularReadPath);
    MessageReceiver receiver = initReceiver();
    receiver.process();
    clientMessages.remove();
    clientMessages.remove();
    Message clientMessage = clientMessages.remove();
    List<String> clientList = clientMessage.getClientList();
    assertEquals(clientMessage.getActionType(), ActionType.CLIENT_LIST);
    assertEquals("Test4", clientList.remove(0));
    assertEquals("Test5", clientList.remove(0));
  }

  @Test
  void processMessages_EndOfStream() throws IOException {
    initConnection(regularReadPath);
    MessageReceiver receiver = initReceiver();
    receiver.attach(dummyObserver);
    receiver.process();
    logMessages.remove();
    Message logMessage = logMessages.remove();
    clientMessages.remove();
    clientMessages.remove();
    clientMessages.remove();
    assertNull(clientMessages.poll());
    assertEquals(logMessage.getActionType(), ActionType.CONNECTION_INTERRUPT);
  }

  @Test
  void processMessages_NoJson_NoMessageIsSaved() throws IOException {
    initConnection(noJsonReadPath);
    MessageReceiver receiver = initReceiver();
    receiver.process();
    assertNull(clientMessages.poll());
  }

  @Test
  void processMessages_NoJson_SecondValidMessage_IsSaved() throws IOException {
    initConnection(noJsonFirstReadPath);
    MessageReceiver receiver = initReceiver();
    receiver.process();
    Message clientMessage = clientMessages.remove();
    assertEquals(clientMessage.getActionType(), ActionType.CLIENT_JOIN_MESSAGE);
    assertEquals(clientMessage.getUsername(), "Test1");
  }

  @Test
  void processMessages_MalformedUsername_SecondValidMessage_IsSaved() throws IOException {
    initConnection(malformedUsernameFirstReadPath);
    MessageReceiver receiver = initReceiver();
    receiver.process();
    Message clientMessage = clientMessages.remove();
    assertEquals(clientMessage.getActionType(), ActionType.CLIENT_JOIN_MESSAGE);
    assertEquals(clientMessage.getUsername(), "Test1");
  }

  @Test
  void processMessages_MissingAction_ThirdValidMessage_IsSaved() throws IOException {
    initConnection(missingActionReadPath);
    MessageReceiver receiver = initReceiver();
    receiver.process();
    Message clientMessage = clientMessages.remove();
    assertEquals(clientMessage.getActionType(), ActionType.CLIENT_JOIN_MESSAGE);
    assertEquals(clientMessage.getUsername(), "Test1");
  }
}
