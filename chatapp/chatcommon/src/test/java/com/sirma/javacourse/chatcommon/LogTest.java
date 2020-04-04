package com.sirma.javacourse.chatcommon;

import static com.sirma.javacourse.chatcommon.Clock.getCurrentDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatcommon.log.Log;
import com.sirma.javacourse.chatcommon.log.LogModel;

class LogTest {
  private Log log;

  @AfterAll
  static void deleteLogFile() {
    String expectedFileName = "LogFile" + getCurrentDate() + ".txt";
    try {
      Path expectedFilePath = Paths.get(".\\src\\test\\resources\\" + expectedFileName);
      Path expectedFilePath1 = Paths.get(".\\src\\test\\" + expectedFileName);
      Files.delete(expectedFilePath);
      Files.delete(expectedFilePath1);
    } catch (IOException e) {

    }
  }

  @Test
  void addMessage_WorkingPath_Directory_CheckCreatesFile() {
    Path path = Paths.get(".\\src\\test\\resources");
    try {
      log = new Log(path);
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
    String expectedFileName = "LogFile" + getCurrentDate() + ".txt";
    Path expectedFilePath = Paths.get(".\\src\\test\\resources\\" + expectedFileName);
    assertTrue(Files.isRegularFile(expectedFilePath));
    assertTrue(Files.isWritable(expectedFilePath));
  }

  @Test
  void addMessage_CheckMessageIsRecorded() throws IOException {
    Path path = Paths.get(".\\src\\test\\resources");
    try {
      log = new Log(path);
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
    log.addMessage("123");
    String expectedFileName = "LogFile" + getCurrentDate() + ".txt";
    Path expectedFilePath = Paths.get(".\\src\\test\\resources\\" + expectedFileName);
    BufferedReader reader = Files.newBufferedReader(expectedFilePath);
    String message = reader.readLine();
    assertEquals("123", message);
  }

  @Test
  void setLogModel_CheckMessageIsShown() {
    Path path = Paths.get(".\\src\\test\\");
    StatusField field = new StatusField(new JTextArea());
    LogModel model = new LogModel(field);
    try {
      log = new Log(path);
      log.setLogModel(model);
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
    String expectedMessage = "456";
    log.addMessage(expectedMessage);
    assertEquals(expectedMessage, field.getTextFromStatusField().substring(1));
  }

  @Test
  void addMessage_WrongPath_NotWritableFile_ThrowsException() {
    Path path = Paths.get(".\\src\\test\\resources\\Server\\ServerUseCaseDiagram.png");
    assertThrows(IOException.class, () -> log = new Log(path));
  }
}
