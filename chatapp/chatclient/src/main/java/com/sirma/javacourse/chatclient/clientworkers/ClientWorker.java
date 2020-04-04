package com.sirma.javacourse.chatclient.clientworkers;

import java.util.ArrayList;
import java.util.List;

/** A runnable and subject implementation that is used to process client requests. */
public abstract class ClientWorker implements Runnable, Subject {
  private List<Observer> observers = new ArrayList<>();

  /**
   * Method is used to attach input observer.
   *
   * @param observer is an observer implementation
   */
  @Override
  public void attach(Observer observer) {
    observers.add(observer);
  }

  /**
   * Method is used to update attached observers with input message.
   *
   * @param message is a message object
   */
  @Override
  public void notifyObservers(Message message) {
    for (Observer observer : observers) {
      observer.update(message);
    }
  }

  /** Method calls the abstract process method. */
  @Override
  public void run() {
    process();
  }

  /** Abstract method used to process client requests. */
  abstract void process();
}
