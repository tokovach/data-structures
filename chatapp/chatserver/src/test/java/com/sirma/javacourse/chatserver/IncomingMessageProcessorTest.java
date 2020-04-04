package com.sirma.javacourse.chatserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatserver.serverworkers.ActionType;
import com.sirma.javacourse.chatserver.serverworkers.IncomingMessageProcessor;
import com.sirma.javacourse.chatserver.serverworkers.Message;

class IncomingMessageProcessorTest {
  LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
  LinkedBlockingQueue<Message> sentMessages = new LinkedBlockingQueue<>();
  String veryLongMessage =
      "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901"
          + "23456789012345678901234567890123456789012345678901234567890123456789012345678901234567"
          + "89012345678901234567890123456789012345678901234567890123456789012345678901234567890123"
          + "45678901234567890123456789012345678901234567890";

  Message laterMessage =
      Message.saveMessage("test4", ActionType.CLIENT_MESSAGE, "12.12.12")
          .clientMessage("Later")
          .build();

  void addMessages() {
    messages.add(
        Message.saveMessage("test", ActionType.CLIENT_MESSAGE, "12.12.12")
            .clientMessage("Hi there")
            .build());
    messages.add(
        Message.saveMessage("test1", ActionType.CLIENT_MESSAGE, "12.12.12")
            .clientMessage("123")
            .build());
    messages.add(
        Message.saveMessage("test2", ActionType.CLIENT_MESSAGE, "12.12.12")
            .clientMessage("Normal message")
            .build());
    messages.add(
        Message.saveMessage("test3", ActionType.CLIENT_MESSAGE, "12.12.12")
            .clientMessage(veryLongMessage)
            .build());
  }

  @Test
  void process_NormalFlow() throws InterruptedException {
    addMessages();
    IncomingMessageProcessor messageProcessor =
        new IncomingMessageProcessor(messages, (m) -> sentMessages.add(m));
    Thread thread = new Thread(messageProcessor);
    thread.start();
    assertEquals("test", sentMessages.take().getUsername());
    assertEquals("123", sentMessages.take().getClientMessage());
    assertEquals(ActionType.CLIENT_MESSAGE, sentMessages.take().getActionType());
    thread.interrupt();
    assertTrue(messages.isEmpty());
  }

  @Test
  void process_IrregularFlow() throws InterruptedException {
    IncomingMessageProcessor messageProcessor =
            new IncomingMessageProcessor(messages, (m) -> sentMessages.add(m));
    Thread thread = new Thread(messageProcessor);
    thread.start();
    addMessages();
    assertEquals("test", sentMessages.take().getUsername());
    assertEquals("123", sentMessages.take().getClientMessage());
    assertEquals(ActionType.CLIENT_MESSAGE, sentMessages.take().getActionType());
    assertTrue(messages.isEmpty());
    messages.add(laterMessage);
    Message processedMessage = sentMessages.take();
    assertEquals("test4", processedMessage.getUsername());
    assertEquals("Later", processedMessage.getClientMessage());
  }
}
