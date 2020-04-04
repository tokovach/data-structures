package com.sirma.javacourse.chatclient;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.log.MessageProvider;
import com.sirma.javacourse.chatcommon.log.NoSuchMessageException;

class MessageProviderTest {
  MessageProvider provider = new MessageProvider(new Locale(""));
  MessageProvider bulgarianProvider = new MessageProvider(new Locale("BG"));

  @Test
  void getMessage_logMessage_MessageIsCorrect_EN() throws NoSuchMessageException {
    String username = "Test";
    String actualMessage =
        provider.getLogMessage(
            Message.saveMessage(ActionType.CLIENT_JOIN_MESSAGE)
                .username(username)
                .time("12.12.12")
                .build());
    String expectedMessage = "User Test joined the chat.";
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void getMessage_logMessage_MessageIsCorrect_BG() throws NoSuchMessageException {
    String username = "Test";
    String actualMessage =
        bulgarianProvider.getLogMessage(
            Message.saveMessage(ActionType.CLIENT_LEAVE_MESSAGE)
                .username(username)
                .time("12.12.12")
                .build());
    String expectedMessage = "Клиентът Test напусна чата.";
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void getMessage_logMessage_MessageContainsTime_EN() throws NoSuchMessageException {
    String username = "Test";
    String actualMessage =
        provider.getLogMessage(
            Message.saveMessage(ActionType.WELCOME_MESSAGE)
                .username(username)
                .time("12.12.12")
                .build());
    assertTrue(actualMessage.matches("<\\d\\d\\.\\d\\d\\.\\d\\d>.+"));
  }

  @Test
  void getMessage_logMessage_ThrowsException_WrongActionType_EN() {
    assertThrows(
        NoSuchMessageException.class,
        () ->
            provider.getLogMessage(
                Message.saveMessage(ActionType.CLIENT_LIST).clientList(null).build()));
  }
}
