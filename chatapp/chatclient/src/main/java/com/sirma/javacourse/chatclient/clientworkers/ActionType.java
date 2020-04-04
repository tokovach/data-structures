package com.sirma.javacourse.chatclient.clientworkers;

/** Enum type that represents the different type of actions the client handles. */
public enum ActionType {
  MESSAGE_RECEIVER_START,
  CLIENT_MESSAGE,
  WELCOME_MESSAGE,
  CLIENT_LIST,
  SEND_USERNAME,
  INVALID_FORMAT_USERNAME,
  UNAVAILABLE_USERNAME,
  CLIENT_JOIN_MESSAGE,
  CLIENT_LEAVE_MESSAGE,
  CLIENT_LEAVE_REQUEST,
  CONNECTION_INTERRUPT,
  RECONNECT_ATTEMPT
}
