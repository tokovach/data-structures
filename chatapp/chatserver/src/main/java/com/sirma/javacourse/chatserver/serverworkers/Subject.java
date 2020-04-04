package com.sirma.javacourse.chatserver.serverworkers;

/** Subject interface that notifies observers. */
public interface Subject {
  /**
   * Method is used to attach an input observer.
   *
   * @param observer is an observer implementation
   */
  void attach(Observer observer);

  /**
   * Method is used to detach an input observer.
   *
   * @param observer is an observer implementation
   */
  void detach(Observer observer);

  /**
   * Method is used to notify observers.
   *
   * @param message is a message object
   */
  void notifyObservers(Message message);
}
