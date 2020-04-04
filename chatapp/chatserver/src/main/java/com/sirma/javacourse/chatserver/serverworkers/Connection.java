package com.sirma.javacourse.chatserver.serverworkers;

import java.io.BufferedReader;
import java.io.PrintWriter;

/** Connection interface used to provide connection streams. */
public interface Connection {

  /**
   * Method is used to get the reader of the connection
   *
   * @return a buffered reader
   */
  BufferedReader read();

  /**
   * Method is used to get the writer of the connection
   *
   * @return a print writer
   */
  PrintWriter write();

  /** Method is used to close connection. */
  void close();
}
