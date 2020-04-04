package com.sirma.javacourse.chatclient;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.clientworkers.MessageSender;
import com.sirma.javacourse.chatclient.clientworkers.Observer;

class MessageSenderTest {
  private static Path writerPath = Paths.get(".\\src\\test\\resources\\sender-write.txt");
  private static Path writerPath1 = Paths.get(".\\src\\test\\resources\\sender-write1.txt");
  private PrintWriter clientWriter;
  private LinkedBlockingQueue<Message> logMessages;
  private Observer dummyObserver =
      new Observer() {
        @Override
        public void update(Message message) {
          logMessages.offer(message);
        }
      };
  private Message infoMessage =
      Message.saveMessage(ActionType.CLIENT_LEAVE_REQUEST).username("Test").build();
  private Message clientMessage =
      Message.saveMessage(ActionType.CLIENT_MESSAGE).username("123").clientMessage("Hi").build();

  private String readMessageFromWriteFile(Path path) throws IOException {
    BufferedReader reader = Files.newBufferedReader(path);
    return reader.readLine();
  }

  @AfterAll
  static void deleteWriteFile() {
    try {
      Files.delete(writerPath);
    } catch (IOException ignored) {
    }
    try {
      Files.delete(writerPath1);
    } catch (IOException ignored) {
    }
  }

  @Test
  void sendMessage_Info() throws IOException, InterruptedException {
    logMessages = new LinkedBlockingQueue<>();
    clientWriter = new PrintWriter(Files.newOutputStream(writerPath), true);
    MessageSender sender = new MessageSender(clientWriter, infoMessage);
    sender.attach(dummyObserver);
    sender.process();
    Message logMessage = logMessages.take();
    String clientMessage = readMessageFromWriteFile(writerPath);
    assertSame(infoMessage, logMessage);
    assertTrue(clientMessage.contains(infoMessage.getActionType().toString()));
  }

  @Test
  void sendMessage_Client() throws IOException, InterruptedException {
    logMessages = new LinkedBlockingQueue<>();
    clientWriter = new PrintWriter(Files.newOutputStream(writerPath1), true);
    MessageSender sender = new MessageSender(clientWriter, clientMessage);
    sender.attach(dummyObserver);
    sender.process();
    Message logMessage = logMessages.take();
    String outputClientMessage = readMessageFromWriteFile(writerPath1);
    assertSame(clientMessage, logMessage);
    assertTrue(outputClientMessage.contains(clientMessage.getClientMessage()));
  }
}
