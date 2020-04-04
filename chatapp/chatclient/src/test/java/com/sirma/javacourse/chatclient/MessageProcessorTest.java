package com.sirma.javacourse.chatclient;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.clientworkers.MessageProcessor;
import com.sirma.javacourse.chatclient.clientworkers.Observer;

class MessageProcessorTest {
  private LinkedBlockingQueue<Message> messages;
  private LinkedBlockingQueue<Message> logMessages;
  private LinkedBlockingQueue<Boolean> booleanApplicationOpen;
  private Message infoMessage =
      Message.saveMessage(ActionType.CLIENT_JOIN_MESSAGE).username("Test").build();
  private Message clientMessage =
      Message.saveMessage(ActionType.CLIENT_MESSAGE).username("123").clientMessage("Hi").build();
  private Message secondInfoMessage =
      Message.saveMessage(ActionType.CLIENT_LEAVE_MESSAGE).username("123").build();
  private Message welcomeMessage =
      Message.saveMessage(ActionType.WELCOME_MESSAGE).username("Test").build();

  private Observer dummyObserver = message -> logMessages.offer(message);

  private void addToOpenApp(boolean b) {
    booleanApplicationOpen.add(b);
  }

  @Test
  void process_NormalFlow() throws InterruptedException {
    booleanApplicationOpen = new LinkedBlockingQueue<>();
    logMessages = new LinkedBlockingQueue<>();
    messages = new LinkedBlockingQueue<>();
    messages.add(infoMessage);
    messages.add(clientMessage);
    MessageProcessor processor = new MessageProcessor(messages, this::addToOpenApp);
    processor.attach(dummyObserver);
    Thread thread = new Thread(processor);
    thread.start();
    Message logMessage1 = logMessages.take();
    Message logMessage2 = logMessages.take();
    thread.interrupt();
    assertEquals(infoMessage.getActionType(), logMessage1.getActionType());
    assertEquals(infoMessage.getUsername(), logMessage1.getUsername());
    assertEquals(clientMessage.getActionType(), logMessage2.getActionType());
    assertEquals(clientMessage.getClientMessage(), logMessage2.getClientMessage());
    assertTrue(messages.isEmpty());
    assertTrue(booleanApplicationOpen.isEmpty());
  }

  @Test
  void process_IrregularFlow() throws InterruptedException {
    booleanApplicationOpen = new LinkedBlockingQueue<>();
    logMessages = new LinkedBlockingQueue<>();
    messages = new LinkedBlockingQueue<>();
    messages.add(infoMessage);
    messages.add(clientMessage);
    MessageProcessor processor = new MessageProcessor(messages, this::addToOpenApp);
    processor.attach(dummyObserver);
    Thread thread = new Thread(processor);
    thread.start();
    logMessages.take();
    logMessages.take();
    messages.add(secondInfoMessage);
    Message logMessage1 = logMessages.take();
    thread.interrupt();
    assertEquals(secondInfoMessage.getActionType(), logMessage1.getActionType());
    assertEquals(secondInfoMessage.getUsername(), logMessage1.getUsername());
    assertTrue(messages.isEmpty());
    assertTrue(booleanApplicationOpen.isEmpty());
  }

  @Test
  void process_WelcomeMessage() throws InterruptedException {
    booleanApplicationOpen = new LinkedBlockingQueue<>();
    logMessages = new LinkedBlockingQueue<>();
    messages = new LinkedBlockingQueue<>();
    messages.add(welcomeMessage);
    MessageProcessor processor = new MessageProcessor(messages, this::addToOpenApp);
    processor.attach(dummyObserver);
    Thread thread = new Thread(processor);
    thread.start();
    Message logMessage1 = logMessages.take();
    thread.interrupt();
    assertEquals(welcomeMessage.getActionType(), logMessage1.getActionType());
    assertEquals(welcomeMessage.getUsername(), logMessage1.getUsername());
    assertTrue(booleanApplicationOpen.take());
  }
}
