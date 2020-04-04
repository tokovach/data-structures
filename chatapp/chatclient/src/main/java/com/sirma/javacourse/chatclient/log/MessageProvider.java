package com.sirma.javacourse.chatclient.log;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatcommon.log.NoSuchMessageException;

/** Message provider edits messages for client log. */
public class MessageProvider {
  private ResourceBundle logMessages;
  private ResourceBundle exceptionMessages;

  /**
   * Constructor is used to initialize the language in which we want to get the client log messages.
   *
   * @param locale is a language
   */
  public MessageProvider(Locale locale) {
    this.logMessages = ResourceBundle.getBundle("log-messages", locale);
    this.exceptionMessages = ResourceBundle.getBundle("exception-messages", locale);
  }

  /**
   * Method is used to get log message based on input message.
   *
   * @param message is a message object
   * @return a string log message
   * @throws NoSuchMessageException when there is no such log message corresponding to the action
   *     type of the message input
   */
  public String getLogMessage(Message message) throws NoSuchMessageException {
    ActionType actionType = message.getActionType();
    if (actionType == ActionType.CLIENT_MESSAGE) {
      return formatClientMessage(
          message.getTime(), message.getUsername(), message.getClientMessage());
    }
    String logMessage = getInfoMessageFromBundle(message.getActionType());
    return formatMessage(message.getTime(), logMessage, message.getUsername());
  }

  /**
   * Method gets the info message from resource bundle based on input action.
   *
   * @param actionType is an enum action type
   * @return a string info message
   * @throws NoSuchMessageException when there is no such log message corresponding to the action *
   *     type input
   */
  private String getInfoMessageFromBundle(ActionType actionType) throws NoSuchMessageException {
    try {
      return logMessages.getString(getBundleKey(actionType));
    } catch (MissingResourceException e) {
      throw new NoSuchMessageException(exceptionMessages.getString("log.message.exception"));
    }
  }

  /**
   * Method returns the string bundle key corresponding to the input action type.
   *
   * @param actionType is an enum action type
   * @return a string bundle key
   */
  private String getBundleKey(ActionType actionType) {
    return actionType.toString().toLowerCase().replace("_", ".");
  }

  /**
   * Method is used to format client message.
   *
   * @param time is a string time input
   * @param username is a string username input
   * @param message is a string client message input
   * @return a string log message
   */
  private String formatClientMessage(String time, String username, String message) {
    return formatTime(time) + formatUsername(username) + message;
  }

  /**
   * Method is used to format info message.
   *
   * @param time is a string time input
   * @param username is a string username input
   * @param infoMessage is a string info message input
   * @return a string log message
   */
  private String formatMessage(String time, String infoMessage, String username) {
    return formatTime(time) + String.format(infoMessage, username);
  }

  /**
   * Method is used to format input time.
   *
   * @param time is a string time input
   * @return a formatted time string
   */
  private String formatTime(String time) {
    return "<" + time + ">";
  }

  /**
   * Method is used to format input username.
   *
   * @param username is a string username input
   * @return a formatted username string
   */
  private String formatUsername(String username) {
    return "<" + username + ">: ";
  }
}
