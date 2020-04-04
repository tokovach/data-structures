package integration;

import static com.sirma.javacourse.chatcommon.Clock.getCurrentDate;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
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

import com.sirma.javacourse.chatclient.Client;
import com.sirma.javacourse.chatclient.clientgui.ClientListModel;
import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatcommon.StatusField;
import com.sirma.javacourse.chatcommon.log.LogModel;

class ClientSendMessageTest {
  ClientSendMessageTest() throws IOException {}

  private InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), 7000);
  private static StatusField statusField;
  private static ClientListModel listModel;
  private static LogModel logModel;
  private static ServerSocket serverSocket;

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
    return Message.saveMessage(actionType).username(username).time("12.12.12").build();
  }

  private String selectUsername(String reason) {
    if (reason.contains("SEND_USERNAME")) {
      return "test123";
    }
    return "test321";
  }

  private void initModels() {
    statusField = new StatusField(new JTextArea());
    listModel = new ClientListModel(statusField);
    logModel = new LogModel(statusField);
  }

  @BeforeAll
  static void startServer() throws IOException {
    serverSocket = new ServerSocket(7000);
  }

  @AfterAll
  static void stopServer() throws IOException {
    serverSocket.close();
  }

  @AfterAll
  static void deleteLog() throws IOException {
    String logFile = "LogFile" + getCurrentDate() + ".txt";
    Path path = Paths.get(".\\src\\test\\resources\\output\\" + logFile);
    Files.delete(path);
  }

  @Test
  void connect_CheckClientMessageSent() throws IOException {
    initModels();
    Client client =
        new Client(
            socketAddress,
            Paths.get(".\\src\\test\\resources\\output"),
            new Locale(""),
            this::selectUsername,
            (b) -> {});
    client.setListModel(listModel);
    client.setLogModel(logModel);
    client.connect();
    Socket clientConnection = serverSocket.accept();
    String sendUsernameMessage = readMessage(clientConnection);
    sendMessage(ActionType.INVALID_FORMAT_USERNAME, clientConnection, "test123");
    String secondUsernameMessage = readMessage(clientConnection);
    sendMessage(ActionType.WELCOME_MESSAGE, clientConnection, "test321");
    client.sendMessage("123");
    String clientMessage = readMessage(clientConnection);
    while (true) {
      String welcomeMessage = statusField.getTextFromStatusField();
      if (welcomeMessage != null && !welcomeMessage.isEmpty()) {
        assertTrue(sendUsernameMessage.contains(ActionType.SEND_USERNAME.toString()));
        assertTrue(sendUsernameMessage.contains("test123"));
        assertTrue(secondUsernameMessage.contains(ActionType.SEND_USERNAME.toString()));
        assertTrue(secondUsernameMessage.contains("test321"));
        assertTrue(welcomeMessage.contains("Welcome"));
        assertTrue(welcomeMessage.contains("test321"));
        assertTrue(clientMessage.contains(ActionType.CLIENT_MESSAGE.toString()));
        assertTrue(clientMessage.contains("123"));
        assertTrue(clientMessage.contains("test321"));
        client.disconnect();
        clientConnection.close();
        return;
      }
    }
  }
}
