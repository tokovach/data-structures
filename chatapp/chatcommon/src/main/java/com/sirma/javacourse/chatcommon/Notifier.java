package com.sirma.javacourse.chatcommon;

import javax.swing.*;

/** Notifier implementation that uses option panes to display error messages. */
public class Notifier {
  /**
   * showError uses a message dialog to display an error with input message.
   *
   * @param message is an input string
   */
  public static void showError(String message) {
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
  }
}
