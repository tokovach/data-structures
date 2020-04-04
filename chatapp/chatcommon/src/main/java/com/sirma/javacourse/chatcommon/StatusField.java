package com.sirma.javacourse.chatcommon;

import javax.swing.*;

/** Status Field that is used to add new messages to input status area. */
public class StatusField {
  private JTextArea textArea;

  /**
   * Constructor is used to set the status area we edit.
   *
   * @param textArea is a JTextArea object
   */
  public StatusField(JTextArea textArea) {
    this.textArea = textArea;
  }

  /**
   * Method appends a new message to the status area on a new line.
   *
   * @param message is a String message we want to add.
   */
  public void addNewTextToStatusField(String message) {
    textArea.append("\n" + message);
  }

  /**
   * Method gets all the messages from the status area in a String and returns them
   *
   * @return a String
   */
  public String getTextFromStatusField() {
    return textArea.getText();
  }

  /**
   * Method changes the text in the status field to the input one.
   *
   * @param text is a String text
   */
  public void setTextToStatusField(String text) {
    textArea.setText(text);
  }
}
