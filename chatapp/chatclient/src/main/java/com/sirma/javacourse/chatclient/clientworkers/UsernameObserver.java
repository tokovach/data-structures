package com.sirma.javacourse.chatclient.clientworkers;

import java.util.function.Consumer;

/**
 * An observer implementation that is used to update a consumer of change reasons for client
 * username
 */
public class UsernameObserver implements Observer {
  private Consumer<ActionType> changeUsername;

  /**
   * Constructor is used to update a consumer interface for changing client username due to a action
   * type reason.
   *
   * @param changeUsername is a consumer interface that accepts enum action types.
   */
  public UsernameObserver(Consumer<ActionType> changeUsername) {
    this.changeUsername = changeUsername;
  }

  /**
   * Method checks if incoming action is to inform for username being unavailable or in invalid
   * format and then proceeds to update the consumer interface with the reason to change the
   * username.
   *
   * @param message is a message object
   */
  @Override
  public void update(Message message) {
    ActionType actionType = message.getActionType();
    if (actionType == ActionType.UNAVAILABLE_USERNAME
        || actionType == ActionType.INVALID_FORMAT_USERNAME) {
      changeUsername.accept(actionType);
    }
  }
}
