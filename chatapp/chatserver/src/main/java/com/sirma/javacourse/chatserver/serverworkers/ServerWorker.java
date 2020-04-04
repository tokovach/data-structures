package com.sirma.javacourse.chatserver.serverworkers;

import java.util.ArrayList;
import java.util.List;

/** A subject and runnable implementation that is used to process server requests. */
public abstract class ServerWorker implements Subject, Runnable {
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
   * Method is used to detach input observer.
   *
   * @param observer is an observer implementation
   */
  @Override
  public void detach(Observer observer) {
    observers.remove(observer);
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

  /** Abstract method used to process server requests. */
  public abstract void process();
}
