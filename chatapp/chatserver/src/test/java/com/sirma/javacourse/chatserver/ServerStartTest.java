package com.sirma.javacourse.chatserver;

import static com.sirma.javacourse.chatcommon.Clock.getCurrentDate;
import static java.net.InetAddress.getLocalHost;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import javax.swing.*;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatcommon.StatusField;
import com.sirma.javacourse.chatcommon.log.LogModel;
import com.sirma.javacourse.chatcommon.log.NoSuchMessageException;
import com.sirma.javacourse.chatserver.log.MessageProvider;
import com.sirma.javacourse.chatserver.servergui.ClientListModel;
import com.sirma.javacourse.chatserver.serverworkers.ActionType;
import com.sirma.javacourse.chatserver.serverworkers.Message;

class ServerStartTest {
  private static JTextArea textArea = new JTextArea();
  private static StatusField statusField = new StatusField(textArea);
  private static ClientListModel listModel = new ClientListModel(statusField);
  private static LogModel logModel = new LogModel(statusField);
  private static ServerActions serverActions;
  private MessageProvider factory = new MessageProvider(Locale.ENGLISH);

  void sendMessage(ActionType actionType, Socket clientSocket, String username) throws IOException {
    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
    Message message = createMessage(username, actionType);
    writer.println(new JSONObject(message));
  }

  String readMessage(Socket clientSocket) throws IOException {
    BufferedReader reader =
        new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    return reader.readLine();
  }

  Message createMessage(String username, ActionType actionType) {
    return Message.saveMessage(username, actionType, "12.12.12").build();
  }

  @BeforeAll
  static void initServer() throws IOException {
    serverActions = new Server(Locale.ENGLISH, 7001, Paths.get(".\\src\\test\\resources\\output"));
    serverActions.setServerLogModel(logModel);
    serverActions.setClientListModel(listModel);
    serverActions.startServer();
  }

  @AfterAll
  static void closeServer() {
    serverActions.stopServer();
  }

  @AfterAll
  static void deleteLog() throws IOException {
    String logFile = "LogFile" + getCurrentDate() + ".txt";
    Path path = Paths.get(".\\src\\test\\resources\\output\\" + logFile);
    Files.delete(path);
  }

  @Test
  void startServer_CheckLog_ClientConnectDisconnect()
      throws IOException, NoSuchMessageException, InterruptedException {
    statusField.setTextToStatusField("");
    String username = "Test";
    Socket clientSocket = new Socket(getLocalHost(), 7001);
    sendMessage(ActionType.SEND_USERNAME, clientSocket, username);
    String expectedClientConnectedMessage =
        factory.getLogMessage(createMessage(username, ActionType.SEND_USERNAME)).substring(10);
    String expectedClientAddedToListMessage =
        factory.getLogMessage(createMessage(username, ActionType.ADD_CLIENT_TO_LIST)).substring(10);
    String expectedClientReceiverMessage =
        factory
            .getLogMessage(createMessage(username, ActionType.MESSAGE_RECEIVER_START))
            .substring(10);
    String expectedClientJoinedMessage =
        factory
            .getLogMessage(createMessage(username, ActionType.CLIENT_JOIN_MESSAGE))
            .substring(10);
    String welcomeMessage1 = readMessage(clientSocket);
    assertTrue(welcomeMessage1.contains(ActionType.WELCOME_MESSAGE.toString()));
    String listMessage1 = readMessage(clientSocket);
    assertTrue(listMessage1.contains(ActionType.CLIENT_LIST.toString()));
    sendMessage(ActionType.CLIENT_LEAVE_REQUEST, clientSocket, username);
    Thread.sleep(500);
    String log = statusField.getTextFromStatusField();
    assertTrue(log.contains(expectedClientAddedToListMessage));
    assertTrue(log.contains(expectedClientConnectedMessage));
    assertTrue(log.contains(expectedClientReceiverMessage));
    assertTrue(log.contains(expectedClientJoinedMessage));
    String expectedClientDisconnectMessage =
        factory.getLogMessage(createMessage(username, ActionType.CLIENT_DISCONNECT)).substring(10);
    String expectedClientRemovedMessage =
        factory
            .getLogMessage(createMessage(username, ActionType.REMOVE_CLIENT_FROM_LIST))
            .substring(10);
    clientSocket.close();
    log = statusField.getTextFromStatusField();
    assertTrue(log.contains(expectedClientDisconnectMessage));
    assertFalse(log.contains("\n" + username));
    assertTrue(log.contains(expectedClientRemovedMessage));
  }

  @Test
  void startServer_CheckLog_ClientUsernameVerify() throws IOException {
    statusField.setTextToStatusField("");
    String username = "Test";
    Socket clientSocket = new Socket(getLocalHost(), 7001);
    sendMessage(ActionType.SEND_USERNAME, clientSocket, "");
    String failMessage1 = readMessage(clientSocket);
    assertTrue(failMessage1.contains(ActionType.INVALID_FORMAT_USERNAME.toString()));
    sendMessage(ActionType.SEND_USERNAME, clientSocket, "waddawdawda");
    String failMessage2 = readMessage(clientSocket);
    assertTrue(failMessage2.contains(ActionType.INVALID_FORMAT_USERNAME.toString()));
    sendMessage(ActionType.SEND_USERNAME, clientSocket, username);
    String welcomeMessage1 = readMessage(clientSocket);
    assertTrue(welcomeMessage1.contains(ActionType.WELCOME_MESSAGE.toString()));
    String listMessage1 = readMessage(clientSocket);
    assertTrue(listMessage1.contains(ActionType.CLIENT_LIST.toString()));
    clientSocket.close();
  }

  @Test
  void startServer_CheckLog_ClientInterrupted()
      throws IOException, NoSuchMessageException, InterruptedException {
    statusField.setTextToStatusField("");
    String username = "Test2";
    Socket clientSocket = new Socket(getLocalHost(), 7001);
    sendMessage(ActionType.SEND_USERNAME, clientSocket, username);
    String welcomeMessage1 = readMessage(clientSocket);
    assertTrue(welcomeMessage1.contains(ActionType.WELCOME_MESSAGE.toString()));
    String listMessage1 = readMessage(clientSocket);
    assertTrue(listMessage1.contains(ActionType.CLIENT_LIST.toString()));
    clientSocket.close();
    Thread.sleep(500);
    String expectedClientDisconnectMessage =
        factory.getLogMessage(createMessage(username, ActionType.CLIENT_INTERRUPT)).substring(10);
    String expectedClientRemovedMessage =
        factory
            .getLogMessage(createMessage(username, ActionType.REMOVE_CLIENT_FROM_LIST))
            .substring(10);
    String log = statusField.getTextFromStatusField();
    assertTrue(log.contains(expectedClientDisconnectMessage));
    assertFalse(log.contains("\n" + username));
    assertTrue(log.contains(expectedClientRemovedMessage));
  }

  @Test
  void startServer_CheckClientSendReceiveMessage() throws IOException {
    statusField.setTextToStatusField("");
    String username = "Test4";
    String username1 = "Test5";
    String message = "First message";
    Socket clientSocket = new Socket(getLocalHost(), 7001);
    Socket clientSocket1 = new Socket(getLocalHost(), 7001);
    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
    PrintWriter writer1 = new PrintWriter(clientSocket1.getOutputStream(), true);
    writer.println(new JSONObject(createMessage(username, ActionType.SEND_USERNAME)));
    String welcomeMessage1 = readMessage(clientSocket);
    assertTrue(welcomeMessage1.contains(ActionType.WELCOME_MESSAGE.toString()));
    String listMessage1 = readMessage(clientSocket);
    assertTrue(listMessage1.contains(ActionType.CLIENT_LIST.toString()));
    assertFalse(listMessage1.contains(username1));
    writer1.println(new JSONObject(createMessage(username1, ActionType.SEND_USERNAME)));
    String welcomeMessage2 = readMessage(clientSocket1);
    assertTrue(welcomeMessage2.contains(ActionType.WELCOME_MESSAGE.toString()));
    String listMessage2 = readMessage(clientSocket1);
    assertTrue(listMessage2.contains(ActionType.CLIENT_LIST.toString()));
    assertTrue(listMessage2.contains(username));
    String joinMessage = readMessage(clientSocket);
    assertTrue(joinMessage.contains(ActionType.CLIENT_JOIN_MESSAGE.toString()));
    assertTrue(joinMessage.contains(username1));
    writer.println(
        new JSONObject(
            Message.saveMessage(username, ActionType.CLIENT_MESSAGE, "12.12.12")
                .clientMessage(message)
                .build()));
    String messageClient1 = readMessage(clientSocket);
    String messageClient2 = readMessage(clientSocket1);
    clientSocket.close();
    clientSocket1.close();
    assertTrue(messageClient1.contains(ActionType.CLIENT_MESSAGE.toString()));
    assertTrue(messageClient1.contains(message));
    assertTrue(messageClient2.contains(ActionType.CLIENT_MESSAGE.toString()));
    assertTrue(messageClient2.contains(message));
  }
}
