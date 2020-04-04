package com.sirma.javacourse.chatclient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.commands.SendUsernameCommand;

class SendUsernameCommandTest {
  private static Path writePathUser =
      Paths.get(".\\src\\test\\resources\\output-user1.txt");
  private Connection clientConnection = Mockito.mock(ClientConnection.class);
  private PrintWriter clientWriter;

  private String readFromFile(Path path) throws IOException {
    BufferedReader reader = Files.newBufferedReader(path);
    return reader.readLine();
  }

  @AfterAll
  static void deleteWriteFile() {
    try {
      Files.delete(writePathUser);
    } catch (IOException ignored) {
    }
  }

  @Test
  void sendMessage_CheckDelivered() throws IOException {
    clientWriter = new PrintWriter(Files.newOutputStream(writePathUser), true);
    String username = "User123";
    String output;
    SendUsernameCommand messageCommand = new SendUsernameCommand(clientConnection);
    Mockito.when(clientConnection.getUsername()).thenReturn(username);
    Mockito.when(clientConnection.write()).thenReturn(clientWriter);
    messageCommand.execute();
    JSONObject expectedOutput =
        new JSONObject(Message.saveMessage(ActionType.SEND_USERNAME).username(username).build());
    while (true) {
      output = readFromFile(writePathUser);
      if (output != null) {
        assertEquals(expectedOutput.toString(), output);
        return;
      }
    }
  }

  @Test
  void sendMessage_CheckNotDelivered() throws IOException {
    clientWriter = new PrintWriter(Files.newOutputStream(writePathUser), true);
    String username = null;
    String output;
    SendUsernameCommand messageCommand = new SendUsernameCommand(clientConnection);
    Mockito.when(clientConnection.getUsername()).thenReturn(username);
    Mockito.when(clientConnection.write()).thenReturn(clientWriter);
    messageCommand.execute();
    JSONObject expectedOutput =
        new JSONObject(Message.saveMessage(ActionType.SEND_USERNAME).username(username).build());
    for (int i = 0; i < 10; i++) {
      output = readFromFile(writePathUser);
      if (output != null) {
        Assertions.fail();
        return;
      }
    }
  }
}
