package com.sirma.javacourse.chatserver.serverworkers;

import java.util.Collection;

import org.json.JSONObject;

/** Server worker implementation that sends a message to a collection of client connections. */
public class MessageSender extends ServerWorker {
  private Message message;
  private Collection<Connection> connections;

  /**
   * Constructor is used to initialize the message we want to send and the collection of connections
   * to which we want to send it to.
   *
   * @param message is a message object
   * @param connections is a collection of connection implementations
   */
  public MessageSender(Message message, Collection<Connection> connections) {
    this.message = message;
    this.connections = connections;
  }

  /** Method sends a message to connections and then notifies observers. */
  @Override
  public void process() {
    sendMessage();
    notifyObservers(message);
  }

  /** Method wraps the message in a json and sends it to the connections. */
  private void sendMessage() {
    JSONObject messageJSON = new JSONObject(message);
    for (Connection connection : connections) {
      connection.write().println(messageJSON);
    }
  }
}
