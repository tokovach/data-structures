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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.commands.SendLeaveMessageCommand;

class SendLeaveMessageCommandTest {
  private static Path writePathLeave =
      Paths.get(".\\src\\test\\resources\\output-leave1.txt");
  private Connection clientConnection = Mockito.mock(ClientConnection.class);
  private PrintWriter clientWriter;

  private String readFromFile(Path path) throws IOException {
    BufferedReader reader = Files.newBufferedReader(path);
    return reader.readLine();
  }

  @AfterAll
  static void deleteWriteFile() {
    try {
      Files.delete(writePathLeave);
    } catch (IOException ignored) {
    }
  }

  @Test
  void sendMessage_CheckDelivered() throws IOException {
    clientWriter = new PrintWriter(Files.newOutputStream(writePathLeave), true);
    String username = "User123";
    String output;
    SendLeaveMessageCommand messageCommand = new SendLeaveMessageCommand(clientConnection);
    Mockito.when(clientConnection.getUsername()).thenReturn(username);
    Mockito.when(clientConnection.write()).thenReturn(clientWriter);
    messageCommand.execute();
    JSONObject expectedOutput =
        new JSONObject(
            Message.saveMessage(ActionType.CLIENT_LEAVE_REQUEST).username(username).build());
    while (true) {
      output = readFromFile(writePathLeave);
      if (output != null) {
        assertEquals(expectedOutput.toString(), output);
        return;
      }
    }
  }
}
