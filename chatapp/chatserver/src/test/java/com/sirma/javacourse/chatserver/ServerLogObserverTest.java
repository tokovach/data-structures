package com.sirma.javacourse.chatserver;

import static com.sirma.javacourse.chatcommon.Clock.getCurrentDate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatcommon.log.Log;
import com.sirma.javacourse.chatcommon.log.NoSuchMessageException;
import com.sirma.javacourse.chatserver.log.MessageProvider;
import com.sirma.javacourse.chatserver.log.ServerLogObserver;
import com.sirma.javacourse.chatserver.serverworkers.ActionType;
import com.sirma.javacourse.chatserver.serverworkers.Message;

class ServerLogObserverTest {
  private static Log serverLog;
  private MessageProvider provider = new MessageProvider(Locale.ENGLISH);
  private ServerLogObserver logObserver;

  @BeforeAll
  static void initLog() {
    Path path = Paths.get(".\\src\\test\\resources");
    try {
      serverLog = new Log(path);
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @AfterAll
  static void deleteFile() throws IOException {
    String expectedFileName = "LogFile" + getCurrentDate() + ".txt";
    Path expectedFilePath = Paths.get(".\\src\\test\\resources\\" + expectedFileName);
    if (Files.isRegularFile(expectedFilePath)) {
      Files.delete(expectedFilePath);
    }
  }

  @Test
  void update_MessageIsRecordedInLog() throws IOException, NoSuchMessageException {
    logObserver = new ServerLogObserver(serverLog, Locale.ENGLISH);
    Message inputMessage =
        Message.saveMessage("Test", ActionType.ADD_CLIENT_TO_LIST, "12.12.12").build();
    logObserver.update(inputMessage);
    String expectedFileName = "LogFile" + getCurrentDate() + ".txt";
    Path filePath = Paths.get(".\\src\\test\\resources\\" + expectedFileName);
    BufferedReader reader = Files.newBufferedReader(filePath);
    String actualMessage = reader.readLine();
    String expectedMessage = provider.getLogMessage(inputMessage);
    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void update_WrongActionType_NoUnhandledException() {
    Message inputMessage =
            Message.saveMessage("Test", ActionType.CLIENT_MESSAGE, "12.12.12").build();
    logObserver = new ServerLogObserver(serverLog, Locale.ENGLISH);
    assertDoesNotThrow(
        () -> logObserver.update(inputMessage));
  }
}
