package com.sirma.javacourse.chatclient.log;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.clientworkers.Observer;
import com.sirma.javacourse.chatcommon.log.Log;
import com.sirma.javacourse.chatcommon.log.NoSuchMessageException;

/** An observer implementation that updates the client log. */
public class ClientLogObserver implements Observer {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientLogObserver.class);
  private Log clientLog;
  private MessageProvider messageProvider;

  /**
   * Constructor is used to initialize the client log that is going to be updated as well as the
   * locale.
   *
   * @param clientLog is log object
   * @param locale is the language in which we want to receive log messages
   */
  public ClientLogObserver(Log clientLog, Locale locale) {
    this.clientLog = clientLog;
    this.messageProvider = new MessageProvider(locale);
  }

  /**
   * Method checks if message action type is valid to be displayed in client log and then proceeds
   * to add it to the log.
   *
   * @param message is a message object
   */
  @Override
  public synchronized void update(Message message) {
    if (isActionTypeLoggable(message)) {
      try {
        clientLog.addMessage(messageProvider.getLogMessage(message));
      } catch (NoSuchMessageException e) {
        LOGGER.error(String.format(e.getMessage(), message.getActionType()));
      }
    }
  }

  /**
   * Method checks if action type is valid for logging.
   *
   * @param message is a message object
   * @return a boolean
   */
  private boolean isActionTypeLoggable(Message message) {
    ActionType actionType = message.getActionType();
    return actionType != ActionType.CLIENT_LIST
        && actionType != ActionType.INVALID_FORMAT_USERNAME
        && actionType != ActionType.UNAVAILABLE_USERNAME
        && actionType != ActionType.MESSAGE_RECEIVER_START
        && actionType != ActionType.CONNECTION_INTERRUPT;
  }
}
