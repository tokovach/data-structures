package integration;

import static com.sirma.javacourse.chatcommon.Clock.getCurrentDate;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;

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

class ClientReconnectTest {
  ClientReconnectTest() throws IOException {}

  private InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), 7000);
  private static StatusField logStatusField;
  private static StatusField listStatusField;
  private static ClientListModel listModel;
  private static LogModel logModel;
  private LinkedBlockingQueue<Boolean> openApplication;
  private static ServerSocket serverSocket;

  void sendMessage(ActionType actionType, Socket clientSocket, String username) throws IOException {
    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
    Message message = createMessage(username, actionType);
    writer.println(new JSONObject(message));
  }

  void sendListMessage(Socket clientSocket, String username) throws IOException {
    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
    Message message =
        Message.saveMessage(ActionType.CLIENT_LIST)
            .username(username)
            .clientList(Arrays.asList("one", "two", "three"))
            .build();
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
    listStatusField = new StatusField(new JTextArea());
    logStatusField = new StatusField(new JTextArea());
    listModel = new ClientListModel(listStatusField);
    logModel = new LogModel(logStatusField);
  }

  @BeforeAll
  static void startServer() throws IOException {
    serverSocket = new ServerSocket(7000);
  }

  @AfterAll
  static void deleteLog() throws IOException {
    String logFile = "LogFile" + getCurrentDate() + ".txt";
    Path path = Paths.get(".\\src\\test\\resources\\secondoutput\\" + logFile);
    Files.delete(path);
  }

  @AfterAll
  static void stopServer() throws IOException {
    serverSocket.close();
  }

  @Test
  void connect_CheckReconnect_LogMessages_ListReset() throws IOException {
    initModels();
    openApplication = new LinkedBlockingQueue<>();
    Client client =
        new Client(
            socketAddress,
            Paths.get(".\\src\\test\\resources\\secondoutput"),
            new Locale(""),
            this::selectUsername,
            (b) -> openApplication.add(b));
    client.setListModel(listModel);
    client.setLogModel(logModel);
    client.connect();
    Socket clientConnection = serverSocket.accept();
    String sendUsernameMessage = readMessage(clientConnection);
    assertTrue(sendUsernameMessage.contains(ActionType.SEND_USERNAME.toString()));
    assertTrue(sendUsernameMessage.contains("test123"));
    sendMessage(ActionType.WELCOME_MESSAGE, clientConnection, "test123");
    sendListMessage(clientConnection, "test123");
    while (true) {
      String list = listStatusField.getTextFromStatusField();
      if (list != null && list.contains("three")) {
        assertTrue(list.contains("one"));
        assertTrue(list.contains("two"));
        break;
      }
    }
    clientConnection.close();
    Socket secondClientConnection = serverSocket.accept();
    String usernameMessage = readMessage(secondClientConnection);
    assertTrue(usernameMessage.contains(ActionType.SEND_USERNAME.toString()));
    assertTrue(usernameMessage.contains("test123"));
    assertTrue(listStatusField.getTextFromStatusField().isEmpty());
    sendMessage(ActionType.WELCOME_MESSAGE, secondClientConnection, "test123");
    secondClientConnection.close();
    while (true) {
      String log = logStatusField.getTextFromStatusField();
      if (log.length() > 190) {
        assertTrue(log.contains("reconnect in 5 seconds"));
        assertTrue(log.contains("Welcome"));
        assertTrue(openApplication.poll());
        assertNull(openApplication.poll());
        client.disconnect();
        return;
      }
    }
  }
}
