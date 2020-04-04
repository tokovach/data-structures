package com.sirma.javacourse.chatclient.clientworkers;

import java.io.PrintWriter;

import org.json.JSONObject;

/** A client worker implementation that sends a message through print writer. */
public class MessageSender extends ClientWorker {
  private PrintWriter clientWriter;
  private Message message;

  /**
   * Constructor is used to initialize the print writer and message that we want to send.
   *
   * @param clientWriter is a print writer
   * @param message is a message object
   */
  public MessageSender(PrintWriter clientWriter, Message message) {
    this.clientWriter = clientWriter;
    this.message = message;
  }

  /** Method sends a message in json wrapping through print writer */
  @Override
  public void process() {
    clientWriter.println(new JSONObject(message));
    notifyObservers(message);
  }
}
