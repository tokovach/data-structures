package com.sirma.javacourse.chatclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

import com.sirma.javacourse.chatclient.clientgui.ClientListModel;
import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.commands.CommandProvider;
import com.sirma.javacourse.chatclient.list.ClientList;
import com.sirma.javacourse.chatclient.log.ClientLogObserver;
import com.sirma.javacourse.chatcommon.log.Log;
import com.sirma.javacourse.chatcommon.log.LogModel;

/**
 * Client actions implementation that is used to connect to a server input and send and receive
 * messages.
 */
public class Client implements ClientActions {
  private Log clientLog;
  private ClientList clientList;
  private CommandProvider commandProvider;

  /**
   * Constructor is used to initialize the server address, log file path, language, username
   * selector and application opener.
   *
   * @param serverAddress is an inet socket address
   * @param logPath is a path object
   * @param locale is the language in which we want to receive log messages
   * @param selectUsername is a functional interface for selecting interface
   * @param openApplication is a consumer interface for opening application
   * @throws IOException
   */
  public Client(
      InetSocketAddress serverAddress,
      Path logPath,
      Locale locale,
      Function<String, String> selectUsername,
      Consumer<Boolean> openApplication)
      throws IOException {
    Connection clientConnection = new ClientConnection(serverAddress, selectUsername);
    clientLog = new Log(logPath);
    clientList = new ClientList();
    ClientLogObserver logObserver = new ClientLogObserver(clientLog, locale);
    commandProvider =
        new CommandProvider(clientConnection, logObserver, clientList, openApplication, true);
  }

  /** Method is used to connect a client to server by starting message processor and receiver. */
  @Override
  public void connect() {
    commandProvider.startMessageReceiver();
    commandProvider.startMessageProcessor();
    commandProvider.sendNewUsername(ActionType.SEND_USERNAME);
  }

  /**
   * Method is used to disconnect a client from server by stopping message processor and receiver
   * and sending a leave request message.
   */
  @Override
  public void disconnect() {
    commandProvider.sendLeaveMessage();
    commandProvider.stopMessageProcessor();
    commandProvider.stopMessageReceiver();
  }

  /** Method is used to send a client message to other clients connected to the server. */
  @Override
  public void sendMessage(String message) {
    commandProvider.sendClientMessage(message);
  }

  /**
   * Method is used to set the user interface's list model that shows the active clients.
   *
   * @param listModel is a list model object
   */
  @Override
  public void setListModel(ClientListModel listModel) {
    clientList.attach(listModel);
  }

  /**
   * Method is used to set the user interface's log model that shows the messages from other clients
   * and information messages from server.
   *
   * @param logModel is a log model object
   */
  @Override
  public void setLogModel(LogModel logModel) {
    clientLog.setLogModel(logModel);
  }
}
