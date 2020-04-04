package com.sirma.javacourse.chatserver.clientmap;

import static com.sirma.javacourse.chatcommon.Clock.getCurrentTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.sirma.javacourse.chatserver.serverworkers.ActionType;
import com.sirma.javacourse.chatserver.serverworkers.Connection;
import com.sirma.javacourse.chatserver.serverworkers.Message;
import com.sirma.javacourse.chatserver.serverworkers.Observer;
import com.sirma.javacourse.chatserver.serverworkers.Subject;

/**
 * A subject map implementation that stores client connections and their corresponding usernames.
 */
public class ServerClientMap implements Subject {
  private ConcurrentHashMap<String, Connection> clientMap;
  private List<Observer> observers;

  /** Constructor is used to initialize the client map and observer list. */
  public ServerClientMap() {
    clientMap = new ConcurrentHashMap<>();
    observers = new ArrayList<>();
  }

  /**
   * Method is used to attach an observer implementation to the map.
   *
   * @param observer is an observer implementation
   */
  @Override
  public void attach(Observer observer) {
    observers.add(observer);
  }

  /**
   * Method is used to detach an observer implementation from the map.
   *
   * @param observer is an observer implementation
   */
  @Override
  public void detach(Observer observer) {
    observers.remove(observer);
  }

  /**
   * Method is used to notify observers with an input message.
   *
   * @param message is a message object
   */
  @Override
  public void notifyObservers(Message message) {
    for (Observer observer : observers) {
      observer.update(message);
    }
  }

  /**
   * Method is used to add a new client to the map and notify the observers of the action.
   *
   * @param username is a string username
   * @param connection is a connection implementation
   */
  public void addClient(String username, Connection connection) {
    clientMap.put(username, connection);
    notifyObservers(getMessage(username, ActionType.ADD_CLIENT_TO_LIST));
  }

  /**
   * Method is used to remove a client from the map and notify the observers of the action.
   *
   * @param username is a string username
   */
  public void removeClient(String username) {
    clientMap.remove(username);
    notifyObservers(getMessage(username, ActionType.REMOVE_CLIENT_FROM_LIST));
  }

  /**
   * Method is used to get all the client connections.
   *
   * @return a collection of connection implementations
   */
  public synchronized Collection<Connection> getClients() {
    return clientMap.values();
  }

  /**
   * Method is used to get a particular client connection from the map.
   *
   * @param username is a string username
   * @return a connection object
   */
  public synchronized Connection getClient(String username) {
    return clientMap.get(username);
  }

  /**
   * Method gets the list of usernames in the client map.
   *
   * @return a list of string usernames
   */
  public synchronized List<String> getClientUsernames() {
    return new ArrayList<>(clientMap.keySet());
  }

  /**
   * Method checks if input username is available.
   *
   * @param username is a string username
   * @return a boolean
   */
  public synchronized boolean isUsernameAvailable(String username) {
    for (String clientUsername : clientMap.keySet()) {
      if (clientUsername.equalsIgnoreCase(username)) {
        return false;
      }
    }
    return true;
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
