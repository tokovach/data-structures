package com.sirma.javacourse.chatclient;

import java.io.BufferedReader;
import java.io.PrintWriter;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;

public interface Connection {
  void close();

  BufferedReader read();

  PrintWriter write();

  String getUsername();

  boolean trySetUsername(ActionType reason);

  boolean tryReconnect();
}
