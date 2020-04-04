package com.sirma.javacourse.chatcommon.log;

import com.sirma.javacourse.chatcommon.StatusField;

/** Log model that updates a status field with log messages */
public class LogModel {
  private StatusField logStatusField;

  /**
   * Constructor is used to initialize the status field which is going to be updated.
   *
   * @param statusField is a status field object
   */
  public LogModel(StatusField statusField) {
    this.logStatusField = statusField;
  }

  /**
   * Method adds new input text message to status field.
   *
   * @param text is a string
   */
  public void update(String text) {
    logStatusField.addNewTextToStatusField(text);
  }
}
