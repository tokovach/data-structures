package com.sirma.javacourse.chatserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatserver.serverworkers.ActionType;
import com.sirma.javacourse.chatserver.serverworkers.ClientConnection;
import com.sirma.javacourse.chatserver.serverworkers.Connection;
import com.sirma.javacourse.chatserver.serverworkers.Message;
import com.sirma.javacourse.chatserver.serverworkers.MessageSender;
import com.sirma.javacourse.chatserver.serverworkers.Observer;

class MessageSenderTest {
  private static Path firstWriterPath = Paths.get(".\\src\\test\\resources\\sender-write.txt");
  private static Path secondWriterPath = Paths.get(".\\src\\test\\resources\\sender-write-2.txt");
  private Connection connection;
  private Connection secondConnection;
  private Queue<String> logMessages;
  private Message infoMessage =
      Message.saveMessage("test", ActionType.CLIENT_JOIN_MESSAGE, "12.12.12").build();
  private Message clientMessage =
      Message.saveMessage("123", ActionType.CLIENT_MESSAGE, "12.12.12")
          .clientMessage("start")
          .build();

  private Observer dummyObserver =
      new Observer() {
        @Override
        public void update(Message message) {
          logMessages.offer(message.getActionType().toString() + message.getUsername());
        }
      };

  private void initConnections() throws IOException {
    PrintWriter firstWriter = new PrintWriter(Files.newOutputStream(firstWriterPath), true);
    PrintWriter secondWriter = new PrintWriter(Files.newOutputStream(secondWriterPath), true);
    connection = new ClientConnection(new Socket(), null, firstWriter);
    secondConnection = new ClientConnection(new Socket(), null, secondWriter);
    logMessages = new LinkedList<>();
  }

  private String readMessageFromWriteFile(Path path) throws IOException {
    BufferedReader reader = Files.newBufferedReader(path);
    return reader.readLine();
  }

  @AfterAll
  static void deleteWriteFile() {
    try {
      Files.delete(firstWriterPath);
    } catch (IOException ignored) {
    }
    try {
      Files.delete(secondWriterPath);
    } catch (IOException ignored) {
    }
  }

  @Test
  void sendMessage_CheckInfoMessage() throws IOException {
    initConnections();
    MessageSender sender =
        new MessageSender(infoMessage, Arrays.asList(connection, secondConnection));
    sender.attach(dummyObserver);
    sender.process();
    String logMessage = logMessages.remove();
    assertTrue(logMessage.contains(infoMessage.getUsername()));
    assertTrue(logMessage.contains(infoMessage.getActionType().toString()));
    assertEquals(new JSONObject(infoMessage).toString(), readMessageFromWriteFile(firstWriterPath));
    assertEquals(
        new JSONObject(infoMessage).toString(), readMessageFromWriteFile(secondWriterPath));
  }

  @Test
  void sendMessage_CheckClientMessage() throws IOException {
    initConnections();
    MessageSender sender =
        new MessageSender(clientMessage, Arrays.asList(connection, secondConnection));
    sender.process();
    assertEquals(
        new JSONObject(clientMessage).toString(), readMessageFromWriteFile(firstWriterPath));
    assertEquals(
        new JSONObject(clientMessage).toString(), readMessageFromWriteFile(secondWriterPath));
  }
}
