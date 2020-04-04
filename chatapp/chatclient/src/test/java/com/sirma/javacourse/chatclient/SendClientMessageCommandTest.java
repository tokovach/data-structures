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
import com.sirma.javacourse.chatclient.commands.SendClientMessageCommand;

class SendClientMessageCommandTest {
  private static Path writePathClient =
      Paths.get(".\\src\\test\\resources\\output-client1.txt");
  private static Path writePathClient1 =
      Paths.get(".\\src\\test\\resources\\output-client2.txt");
  private Connection clientConnection = Mockito.mock(ClientConnection.class);
  private PrintWriter clientWriter;

  private String readFromFile(Path path) throws IOException {
    BufferedReader reader = Files.newBufferedReader(path);
    return reader.readLine();
  }

  @AfterAll
  static void deleteWriteFile() {
    try {
      Files.delete(writePathClient);
    } catch (IOException ignored) {
    }
    try {
      Files.delete(writePathClient1);
    } catch (IOException ignored) {
    }
  }

  @Test
  void sendMessage_CheckDelivered() throws IOException {
    clientWriter = new PrintWriter(Files.newOutputStream(writePathClient), true);
    String message = "Hi there";
    String username = "User123";
    String output;
    SendClientMessageCommand messageCommand =
        new SendClientMessageCommand(clientConnection, message);
    Mockito.when(clientConnection.getUsername()).thenReturn(username);
    Mockito.when(clientConnection.write()).thenReturn(clientWriter);
    messageCommand.execute();
    JSONObject expectedOutput =
        new JSONObject(
            Message.saveMessage(ActionType.CLIENT_MESSAGE)
                .username(username)
                .clientMessage(message)
                .build());
    while (true) {
      output = readFromFile(writePathClient);
      if (output != null) {
        assertEquals(expectedOutput.toString(), output);
        return;
      }
    }
  }

  @Test
  void sendMessage_CheckCapitalize() throws IOException {
    clientWriter = new PrintWriter(Files.newOutputStream(writePathClient1), true);
    String message = "hi there";
    String username = "User123";
    String output;
    SendClientMessageCommand messageCommand =
        new SendClientMessageCommand(clientConnection, message);
    Mockito.when(clientConnection.getUsername()).thenReturn(username);
    Mockito.when(clientConnection.write()).thenReturn(clientWriter);
    messageCommand.execute();
    JSONObject expectedOutput =
        new JSONObject(
            Message.saveMessage(ActionType.CLIENT_MESSAGE)
                .username(username)
                .clientMessage("Hi there")
                .build());
    while (true) {
      output = readFromFile(writePathClient1);
      if (output != null) {
        assertEquals(expectedOutput.toString(), output);
        return;
      }
    }
  }

  @Test
  void sendMessage_TooLongMessage() throws IOException {
    clientWriter = new PrintWriter(Files.newOutputStream(writePathClient1), true);
    String message =
        "Hi thereHi Hi thereHi Hi thereHi Hi thereHi Hi thereHi thereHi thereHi thereHi thereHi thereHi thereHi"
            + "thereHi thereHi thereHi thereHi thereHi thereHi thereHi thereHi thereHi thereHi thereHi thereHi"
            + "thereHi thereHi thereHi thereHi there";
    String username = "User123";
    String output;
    SendClientMessageCommand messageCommand =
        new SendClientMessageCommand(clientConnection, message);
    Mockito.when(clientConnection.getUsername()).thenReturn(username);
    Mockito.when(clientConnection.write()).thenReturn(clientWriter);
    messageCommand.execute();
    for (int i = 0; i < 10; i++) {
      output = readFromFile(writePathClient1);
      if (output != null) {
        Assertions.fail();
        return;
      }
    }
  }

  @Test
  void sendMessage_TooShortMessage() throws IOException {
    clientWriter = new PrintWriter(Files.newOutputStream(writePathClient1), true);
    String message = "";
    String username = "User123";
    String output;
    SendClientMessageCommand messageCommand =
        new SendClientMessageCommand(clientConnection, message);
    Mockito.when(clientConnection.getUsername()).thenReturn(username);
    Mockito.when(clientConnection.write()).thenReturn(clientWriter);
    messageCommand.execute();
    for (int i = 0; i < 10; i++) {
      output = readFromFile(writePathClient1);
      if (output != null) {
        Assertions.fail();
        return;
      }
    }
  }
}
