package com.sirma.javacourse.chatcommon.log;

/** Exception is thrown when no such message can be found for the log. */
public class NoSuchMessageException extends Exception {
  public NoSuchMessageException(String message) {
    super(message);
  }
}
