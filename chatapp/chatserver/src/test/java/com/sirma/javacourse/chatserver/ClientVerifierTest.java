package com.sirma.javacourse.chatserver;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatserver.serverworkers.ClientConnection;
import com.sirma.javacourse.chatserver.serverworkers.ClientVerifier;
import com.sirma.javacourse.chatserver.serverworkers.Connection;
import com.sirma.javacourse.chatserver.serverworkers.Message;
import com.sirma.javacourse.chatserver.serverworkers.Observer;

class ClientVerifierTest {
  private static Path standardWriteMessagePath =
      Paths.get(".\\src\\test\\resources\\verifier-write.txt");
  private static Path forbiddenSymbolWritePath =
      Paths.get(".\\src\\test\\resources\\verifier-invalid-write.txt");
  private static Path invalidDisconnectWritePath =
      Paths.get(".\\src\\test\\resources\\verifier-invalid-write.txt");
  private static Path invalidJSONWritePath =
      Paths.get(".\\src\\test\\resources\\verifier-invalid-write.txt");
  private Path successReadFirstPath =
      Paths.get(".\\src\\test\\resources\\verifier-test-success-first-input.txt");
  private Path successReadSecondPath =
      Paths.get(".\\src\\test\\resources\\verifier-test-success-second-input.txt");
  private Path unsuccessReadDisconnect =
      Paths.get(".\\src\\test\\resources\\verifier-test-unsuccessful-input-disconnect.txt");
  private Path unsuccessReadInvalidJSON =
      Paths.get(".\\src\\test\\resources\\verifier-test-unsuccessful-invalid-json.txt");
  private Path unsuccessReadInvalidSymbol =
      Paths.get(".\\src\\test\\resources\\verifier-test-unsuccessful-invalid-symbols.txt");
  private Connection connection;
  private ClientVerifier verifier;
  private HashMap<String, Connection> processedClients;
  private BufferedReader reader;
  private PrintWriter writer;
  private Queue<String> logMessages;
  private Observer dummyObserver =
      new Observer() {
        @Override
        public void update(Message message) {
          logMessages.offer(message.getActionType().toString() + message.getUsername());
        }
      };

  private boolean isUsernameAvailable(String username) {
    return !username.matches("user");
  }

  private void initConnection(Path readerPath, Path writerPath) throws IOException {
    reader = Files.newBufferedReader(readerPath);
    writer = new PrintWriter(Files.newOutputStream(writerPath), true);
    connection = new ClientConnection(new Socket(), reader, writer);
    logMessages = new LinkedList<>();
    processedClients = new HashMap<>();
  }

  private void initVerifier() {
    verifier =
        new ClientVerifier(
            connection,
            this::isUsernameAvailable,
            (username, connection) -> processedClients.put(username, connection));
    verifier.attach(dummyObserver);
  }

  private String readMessageFromWriteFile(Path path) throws IOException {
    BufferedReader reader = Files.newBufferedReader(path);
    String message= "";
    String line;
    while ((line = reader.readLine())!= null){
      message += line;
    }
    return message;
  }

  @AfterAll
  static void deleteWriteFile() {
    try {
      Files.delete(standardWriteMessagePath);
    } catch (IOException ignored) {
    }
    try {
      Files.delete(invalidJSONWritePath);
    } catch (IOException ignored) {
    }
    try {
      Files.delete(forbiddenSymbolWritePath);
    } catch (IOException ignored) {
    }
    try {
      Files.delete(invalidDisconnectWritePath);
    } catch (IOException ignored) {
    }
  }

  @Test
  void verify_ClientSuccessful_FirstTime() throws IOException {
    String username = "test";
    initConnection(successReadFirstPath, standardWriteMessagePath);
    initVerifier();
    verifier.process();
    String logMessage = logMessages.poll();
    assertTrue(processedClients.containsKey(username));
    assertTrue(logMessage.contains("SEND_USERNAME"));
    assertTrue(logMessage.contains(username));
  }

  @Test
  void verify_ClientSuccessful_SecondTime_Username_Taken() throws IOException {
    String username = "test";
    initConnection(successReadSecondPath, standardWriteMessagePath);
    initVerifier();
    verifier.process();
    String firstLogMessage = logMessages.poll();
    String secondLogMessage = logMessages.poll();
    String message = readMessageFromWriteFile(standardWriteMessagePath);
    assertTrue(processedClients.containsKey(username));
    assertTrue(firstLogMessage.contains("SEND_USERNAME"));
    assertTrue(firstLogMessage.contains("user"));
    assertTrue(secondLogMessage.contains(username));
    assertTrue(message.contains("UNAVAILABLE_USERNAME"));
    assertTrue(message.contains("WELCOME_MESSAGE"));
  }

  @Test
  void verify_ClientUnsuccessful_Disconnect() throws IOException {
    initConnection(unsuccessReadDisconnect, invalidDisconnectWritePath);
    String username = "user";
    initVerifier();
    verifier.process();
    String message = readMessageFromWriteFile(invalidDisconnectWritePath);
    assertTrue(message.contains("UNAVAILABLE_USERNAME"));
    assertTrue(message.contains(username));
    assertFalse(processedClients.containsKey(username));
    assertTrue(processedClients.isEmpty());
  }

  @Test
  void verify_ClientUnsuccessful_InvalidSymbols() throws IOException {
    initConnection(unsuccessReadInvalidSymbol, forbiddenSymbolWritePath);
    initVerifier();
    verifier.process();
    String logMessage1 = logMessages.poll();
    String logMessage2 = logMessages.poll();
    String logMessage3 = logMessages.poll();
    String logMessage4 = logMessages.poll();
    String logMessage5 = logMessages.poll();
    String logMessage6 = logMessages.poll();
    String message = readMessageFromWriteFile(forbiddenSymbolWritePath);
    assertTrue(message.contains("INVALID_FORMAT_USERNAME"));
    assertTrue(processedClients.isEmpty());
    assertTrue(logMessage1.contains("SEND_USERNAME"));
    assertTrue(logMessage2.contains("SEND_USERNAME"));
    assertTrue(logMessage3.contains("SEND_USERNAME"));
    assertTrue(logMessage4.contains("SEND_USERNAME"));
    assertTrue(logMessage5.contains("SEND_USERNAME"));
    assertTrue(logMessage6.contains("SEND_USERNAME"));
  }

  @Test
  void verify_ClientUnsuccessful_WrongJSON() throws IOException {
    initConnection(unsuccessReadInvalidJSON, invalidJSONWritePath);
    String username = "user";
    initVerifier();
    verifier.process();
    String message = readMessageFromWriteFile(invalidJSONWritePath);
    assertTrue(message.isEmpty());
    assertFalse(processedClients.containsKey(username));
  }
}
