package com.sirma.javacourse.chatclient.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.clientworkers.Observer;
import com.sirma.javacourse.chatclient.clientworkers.Subject;

/** A subject list implementation that is used for storing client usernames. */
public class ClientList implements Subject {
  private ConcurrentSkipListSet<String> clientUsernames;
  private ArrayList<Observer> observers;

  /** Constructor is used to initialize the client username set and observer list. */
  public ClientList() {
    clientUsernames = new ConcurrentSkipListSet<>();
    observers = new ArrayList<>();
  }

  /**
   * Method attaches an observer to the client list
   *
   * @param observer is an observer implementation
   */
  @Override
  public void attach(Observer observer) {
    observers.add(observer);
  }

  /**
   * Method notifies the observers with an input message
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
   * Method adds a string input username to the client list and if it is not already in there it
   * notifies the observers of the new username.
   *
   * @param username is a string username
   */
  public void add(String username) {
    if (clientUsernames.add(username)) {
      notifyObservers(getMessage(username, ActionType.CLIENT_JOIN_MESSAGE));
    }
  }

  /**
   * Method removes a string input username from the client list and if it is already not there it
   * notifies the observers of the removed username.
   *
   * @param username is a string username
   */
  public void remove(String username) {
    if (clientUsernames.remove(username)) {
      notifyObservers(getMessage(username, ActionType.CLIENT_LEAVE_MESSAGE));
    }
  }

  /**
   * Method adds a list of string input usernames to the client list and for those that are not
   * already in there it notifies the observers of the new usernames.
   *
   * @param usernames is a string username list
   */
  public void addAll(Collection<String> usernames) {
    List<String> tempList = new ArrayList<>();
    for (String username : usernames) {
      if (clientUsernames.add(username)) {
        tempList.add(username);
      }
    }
    if (tempList.size() > 0) {
      notifyObservers(getMessage(tempList));
    }
  }

  /**
   * Method removes all usernames by reinitializing the set of usernames and notifies the observers.
   */
  public void removeAll() {
    clientUsernames = new ConcurrentSkipListSet<>();
    notifyObservers(Message.saveMessage(ActionType.CONNECTION_INTERRUPT).build());
  }

  /**
   * Method checks whether the client list contains an input username
   *
   * @param username is a string username
   * @return a boolean
   */
  public boolean contains(String username) {
    return clientUsernames.contains(username);
  }

  /**
   * Method is used to build a message object.
   *
   * @param username is a string username
   * @param actionType is an action type enum
   * @return a message object
   */
  private Message getMessage(String username, ActionType actionType) {
    return Message.saveMessage(actionType).username(username).build();
  }

  /**
   * Method is used to build a message object.
   *
   * @param usernames is a string usernames list
   * @return a message object
   */
  private Message getMessage(List<String> usernames) {
    return Message.saveMessage(ActionType.CLIENT_LIST).clientList(usernames).build();
  }
}
