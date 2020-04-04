package com.sirma.javacourse.chatserver.log;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sirma.javacourse.chatcommon.log.Log;
import com.sirma.javacourse.chatcommon.log.NoSuchMessageException;
import com.sirma.javacourse.chatserver.serverworkers.ActionType;
import com.sirma.javacourse.chatserver.serverworkers.Message;
import com.sirma.javacourse.chatserver.serverworkers.Observer;

/** An observer implementation that updates the server log. */
public class ServerLogObserver implements Observer {
  private static final Logger LOGGER = LoggerFactory.getLogger(ServerLogObserver.class);
  private Log serverLog;
  private MessageProvider messageProvider;

  /**
   * Constructor is used to initialize the server log that is going to be updated as well as the
   * locale.
   *
   * @param serverLog is log object
   * @param locale is the language in which we want to receive log messages
   */
  public ServerLogObserver(Log serverLog, Locale locale) {
    this.serverLog = serverLog;
    this.messageProvider = new MessageProvider(locale);
  }

  /**
   * Method checks if message action type is valid to be displayed in server log and then proceeds
   * to add it to the log.
   *
   * @param message is a message object
   */
  @Override
  public synchronized void update(Message message) {
    ActionType actionType = message.getActionType();
    if (actionType != ActionType.CLIENT_MESSAGE
        && actionType != ActionType.CLIENT_LEAVE_REQUEST
        && actionType != ActionType.CLIENT_LIST) {
      try {
        serverLog.addMessage(messageProvider.getLogMessage(message));
      } catch (NoSuchMessageException e) {
        LOGGER.error(String.format(e.getMessage(), actionType));
      }
    }
  }
}
