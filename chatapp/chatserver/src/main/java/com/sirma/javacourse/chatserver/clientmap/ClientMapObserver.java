package com.sirma.javacourse.chatserver.clientmap;

import static com.sirma.javacourse.chatcommon.Clock.getCurrentTime;
import static com.sirma.javacourse.chatserver.ServerWorkerProvider.getWorkerThread;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.sirma.javacourse.chatserver.ServerWorkerProvider;
import com.sirma.javacourse.chatserver.serverworkers.ActionType;
import com.sirma.javacourse.chatserver.serverworkers.Connection;
import com.sirma.javacourse.chatserver.serverworkers.Message;
import com.sirma.javacourse.chatserver.serverworkers.Observer;
import com.sirma.javacourse.collections.pagebean.PageBean;

/** Observer implementation that processes new clients and removed clients. */
public class ClientMapObserver implements Observer {
  private static final int SUBLIST_SIZE = 15;
  private ServerClientMap clientMap;
  private ServerWorkerProvider workerProvider;

  /**
   * Constructor is used to initialize the client map and the server worker provider.
   *
   * @param clientMap is a server client map
   * @param workerProvider is a server worker provider
   */
  public ClientMapObserver(ServerClientMap clientMap, ServerWorkerProvider workerProvider) {
    this.clientMap = clientMap;
    this.workerProvider = workerProvider;
  }

  /**
   * Method checks if input message's action type is either for removing or adding a client to a
   * list, and then either processes the new client or sends a disconnect client message to rest of
   * clients.
   *
   * @param message is a message object
   */
  @Override
  public synchronized void update(Message message) {
    String username = message.getUsername();
    ActionType actionType = message.getActionType();
    if (actionType == ActionType.ADD_CLIENT_TO_LIST) {
      processNewClient(username);
      return;
    }
    if (actionType == ActionType.REMOVE_CLIENT_FROM_LIST) {
      sendMessage(username, ActionType.CLIENT_LEAVE_MESSAGE, clientMap.getClients());
    }
  }

  /**
   * Method processes a new client by sending the new client's username to the rest of the users,
   * opens the message receiver for the client and sends the actual client list.
   *
   * @param newClientUsername is a string username
   */
  private void processNewClient(String newClientUsername) {
    sendMessage(
        newClientUsername, ActionType.CLIENT_JOIN_MESSAGE, getAllClientsExcept(newClientUsername));
    startMessageReceiver(newClientUsername);
    sendClientList(newClientUsername);
  }

  /**
   * Method is used to retrieve all client connections except the username input's one.
   *
   * @param newClientUsername is a string username
   * @return a collection of connections
   */
  private Collection<Connection> getAllClientsExcept(String newClientUsername) {
    Collection<Connection> clients = new LinkedList<>(clientMap.getClients());
    clients.remove(clientMap.getClient(newClientUsername));
    return clients;
  }

  /**
   * Method is used to start the message receiver for an input username's connection
   *
   * @param username is a string username
   */
  private void startMessageReceiver(String username) {
    getWorkerThread(workerProvider.getClientMessageReceiver(username)).start();
  }

  /**
   * Method uses a page bean to divide all client username into sub lists and sends them to the new
   * client.
   *
   * @param username is the new client's string username
   */
  private void sendClientList(String username) {
    PageBean pageBean =
        new PageBean<>(SUBLIST_SIZE, new LinkedList<>(clientMap.getClientUsernames()));
    for (int i = 0; i < pageBean.getLastPageNumber(); i++) {
      sendListMessage(username, pageBean.next());
    }
  }

  /**
   * Method uses a message sender to send the client list to the new client.
   *
   * @param username is the client's string username
   * @param clientList is a list of client string usernames
   */
  private void sendListMessage(String username, List<String> clientList) {
    getWorkerThread(
            workerProvider.getMessageSender(
                Message.saveMessage(username, ActionType.CLIENT_LIST, getCurrentTime())
                    .clientList(clientList)
                    .build(),
                Collections.singleton(clientMap.getClient(username))))
        .start();
  }

  /**
   * Method sends an info message to an input of connections.
   *
   * @param username is a string username
   * @param actionType is an action type enum
   * @param connections is the collection of connections to which the message is sent
   */
  private void sendMessage(
      String username, ActionType actionType, Collection<Connection> connections) {
    getWorkerThread(workerProvider.getMessageSender(getMessage(username, actionType), connections))
        .start();
  }

  /**
   * Method is used to build a message from a username and an action type input.
   *
   * @param username is a string username
   * @param actionType is an enum action type
   * @return a message object
   */
  private Message getMessage(String username, ActionType actionType) {
    return Message.saveMessage(username, actionType, getCurrentTime()).build();
  }

}
