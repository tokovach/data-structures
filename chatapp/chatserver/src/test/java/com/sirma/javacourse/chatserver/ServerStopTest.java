package com.sirma.javacourse.chatserver;

import static com.sirma.javacourse.chatcommon.Clock.getCurrentDate;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import javax.swing.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatcommon.StatusField;
import com.sirma.javacourse.chatserver.servergui.ClientListModel;

class ServerStopTest {
  private static ServerActions serverActions;
  private static JTextArea textArea = new JTextArea();
  private static StatusField statusField = new StatusField(textArea);
  private static ClientListModel listModel = new ClientListModel(statusField);

  @BeforeAll
  static void initServer() {
    try {
      serverActions =
          new Server(
              Locale.ENGLISH, 7000, Paths.get(".\\src\\test\\resources\\secondoutput"));
      serverActions.setClientListModel(listModel);
      serverActions.startServer();
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }

  @AfterAll
  static void deleteLog() throws IOException {
    String logFile = "LogFile" + getCurrentDate() + ".txt";
    Path path = Paths.get(".\\src\\test\\resources\\secondoutput\\" + logFile);
    Files.delete(path);
  }


  @Test
  void stopServer_CheckClientCannotConnect() {
    serverActions.stopServer();
    Socket clientSocket = new Socket();
    assertThrows(
        ConnectException.class,
        () -> clientSocket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 7000)));
  }
}
