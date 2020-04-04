package com.sirma.javacourse.chatserver.serverworkers;

/** Enum type that represents the different type of actions the server handles. */
public enum ActionType {
  CLIENT_MESSAGE,
  WELCOME_MESSAGE,
  CLIENT_DISCONNECT,
  CLIENT_INTERRUPT,
  SEND_USERNAME,
  CLIENT_LIST,
  INVALID_FORMAT_USERNAME,
  UNAVAILABLE_USERNAME,
  CLIENT_JOIN_MESSAGE,
  CLIENT_LEAVE_MESSAGE,
  MESSAGE_RECEIVER_START,
  CLIENT_LEAVE_REQUEST,
  ADD_CLIENT_TO_LIST,
  REMOVE_CLIENT_FROM_LIST
}
