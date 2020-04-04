package com.sirma.javacourse.chatserver;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatcommon.log.NoSuchMessageException;
import com.sirma.javacourse.chatserver.log.MessageProvider;
import com.sirma.javacourse.chatserver.serverworkers.ActionType;
import com.sirma.javacourse.chatserver.serverworkers.Message;

class MessageProviderTest {
  private MessageProvider provider = new MessageProvider(new Locale(""));
  private MessageProvider bulgarianProvider = new MessageProvider(new Locale("bg"));

  @Test
  void getMessage_logMessage_MessageIsCorrect_EN() throws NoSuchMessageException {
    String username = "Test";
    String actualMessage =
        provider.getLogMessage(
            Message.saveMessage(username, ActionType.CLIENT_DISCONNECT, "12.12.12").build());
    String expectedMessage = "Test disconnected from server.";
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void getMessage_logMessage_MessageIsCorrect_BG() throws NoSuchMessageException {
    String username = "Test";
    String actualMessage =
        bulgarianProvider.getLogMessage(
            Message.saveMessage(username, ActionType.CLIENT_DISCONNECT, "12.12.12").build());
    String expectedMessage = "Test прекрати връзката със сървъра.";
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void getMessage_logMessage_MessageContainsTime_EN() throws NoSuchMessageException {
    String username = "Test";
    String actualMessage =
        provider.getLogMessage(
            Message.saveMessage(username, ActionType.ADD_CLIENT_TO_LIST, "12.12.12").build());
    assertTrue(actualMessage.matches("<\\d\\d\\.\\d\\d\\.\\d\\d>.+"));
  }

  @Test
  void getMessage_logMessage_ThrowsException_WrongActionType_EN() {
    String username = "Test";
    assertThrows(
        NoSuchMessageException.class,
        () ->
            provider.getLogMessage(
                Message.saveMessage(username, ActionType.CLIENT_MESSAGE, "12.12.12").build()));
  }
}
