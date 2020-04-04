package com.sirma.javacourse.chatserver.servergui;

import static com.sirma.javacourse.chatcommon.Notifier.showError;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import com.sirma.javacourse.chatcommon.Language;

/** Dialog class that is used to start up the application. */
public class StartUpDialog {
  private ResourceBundle labels;
  private ResourceBundle errors;
  private JFileChooser fileChooser;
  private Locale locale;
  private Integer port;
  private Path logFilePath;
  private Language inputLanguage;
  private Language[] languages = Language.values();

  /** Constructor is used to initialize the default text bundles and file chooser. */
  public StartUpDialog() {
    initDefaultBundles();
    initFileChooser();
  }

  /** Method is used to set up the server and start the application. */
  public void startApp() {
    if (!trySetUpServer()) {
      return;
    }
    startServerApp();
  }

  /** Method is used to initialize the default text bundles in english. */
  private void initDefaultBundles() {
    labels = ResourceBundle.getBundle("gui-labels", Locale.ENGLISH);
    errors = ResourceBundle.getBundle("error-messages", Locale.ENGLISH);
  }

  /** Method is used to initialize the file chooser for the log file directory. */
  private void initFileChooser() {
    fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
  }

  /**
   * Method attempts to set up server and interface settings including language, port and log file
   * path directory.
   *
   * @return boolean whether set up was successful
   */
  private boolean trySetUpServer() {
    if (!tryChooseLanguage()) {
      return false;
    }
    if (!tryChoosePort()) {
      return false;
    }
    return tryChooseLogFileDirectory();
  }

  /** Method initializes the server main interface and starts the application. */
  private void startServerApp() {
    try {
      ServerGUI serverGUI = new ServerGUI(locale, port, logFilePath);
      SwingUtilities.invokeLater(serverGUI::startApp);
    } catch (IOException e) {
      handleServerIssues(e.getMessage());
    }
  }

  /**
   * Method attempts to select a language for the server user interface and returns true if
   * successful.
   *
   * @return a boolean
   */
  private boolean tryChooseLanguage() {
    selectLanguage();
    if (inputLanguage != null) {
      locale = new Locale(getLanguageCode());
      labels = ResourceBundle.getBundle("gui-labels", locale);
      errors = ResourceBundle.getBundle("error-messages", locale);
      return true;
    }
    return false;
  }

  /**
   * Method is used to try choosing log file directory and depending on whether it is valid returns
   * true or false.
   *
   * @return a boolean
   */
  private boolean tryChooseLogFileDirectory() {
    fileChooser.setDialogTitle(labels.getString("log.file.dir.chooser.title"));
    int returnValue = fileChooser.showDialog(null, labels.getString("select.control"));
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      try {
        Path tempFilePath = getPath();
        if (isPathValid(tempFilePath)) {
          logFilePath = tempFilePath;
          return true;
        }
      } catch (InvalidPathException e) {
        return false;
      }
    }
    return false;
  }

  /**
   * Method is used to select a port number. If port is valid method will return true, if invalid
   * the method with recurse, if no output is provided, method will return false.
   *
   * @return a boolean
   */
  private boolean tryChoosePort() {
    String portInput =
        JOptionPane.showInputDialog(null, labels.getString("select.port.number"), 7000);
    if (portInput == null) {
      return false;
    }
    try {
      formatPortNumber(portInput);
    } catch (NumberFormatException e) {
      showError(errors.getString("invalid.port.format"));
      tryChoosePort();
    }
    return true;
  }

  /** Method is used to open a pop-up dialog to select language of user interface. */
  private void selectLanguage() {
    inputLanguage =
        (Language)
            JOptionPane.showInputDialog(
                null,
                labels.getString("select.language"),
                labels.getString("server.frame"),
                JOptionPane.QUESTION_MESSAGE,
                null,
                languages,
                Language.ENGLISH);
  }

  /**
   * Method is used to format the string port input.
   *
   * @param portInput is a string input
   * @throws NumberFormatException when port number contains non number chars.
   */
  private void formatPortNumber(String portInput) throws NumberFormatException {
    port = Integer.parseInt(portInput);
    if (!isPortNumberValid()) {
      showError(errors.getString("port.out.of.range"));
      tryChoosePort();
    }
  }

  /**
   * Method checks whether port number is in the 0-65535 range.
   *
   * @return a boolean
   */
  private boolean isPortNumberValid() {
    return port >= 0 && port <= 65535;
  }

  /**
   * Method is used to get selected path from file chooser
   *
   * @return a path object
   * @throws InvalidPathException when an invalid path has been input
   */
  private Path getPath() throws InvalidPathException {
    return fileChooser.getSelectedFile().toPath();
  }

  /**
   * Method checks whether input path exists and whether it is a directory.
   *
   * @param path is the path we want to validate
   * @return a boolean
   */
  private boolean isPathValid(Path path) {
    return Files.exists(path) && Files.isDirectory(path);
  }

  /**
   * Method is used to handle connection problems. It asks for new port number and them starts the
   * server appp if one is provided, otherwise closing the application.
   */
  private void handleServerIssues(String exceptionMessage) {
    port = null;
    displayError("port.is.unavailable", String.valueOf(port));
    tryChoosePort();
    if (port == null) {
      return;
    }
    startServerApp();
  }

  /**
   * Method is used to display a pop up error message
   *
   * @param errorKey is the error bundle string key
   * @param variable is the variable being formatted in the message
   */
  private void displayError(String errorKey, String variable) {
    String errorMessage = String.format(errors.getString(errorKey), variable);
    showError(errorMessage);
  }

  /**
   * Method is used to get the language code.
   *
   * @return a string language code
   */
  private String getLanguageCode() {
    switch (inputLanguage) {
      case BULGARIAN:
        return "bg";
      default:
        return "en";
    }
  }
}
