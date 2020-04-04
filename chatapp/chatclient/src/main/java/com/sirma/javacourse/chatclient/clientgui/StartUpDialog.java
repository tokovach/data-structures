package com.sirma.javacourse.chatclient.clientgui;

import static com.sirma.javacourse.chatcommon.Notifier.showError;

import java.io.IOException;
import java.net.InetSocketAddress;
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
  private InetSocketAddress serverAddress;
  private Path logFilePath;
  private Language inputLanguage;
  private Language[] languages = Language.values();

  /** Constructor is used to initialize the default text bundles and file chooser. */
  public StartUpDialog() {
    initDefaultBundles();
    initFileChooser();
  }

  /** Method is used to set up the connection and start the application. */
  public void startApp() {
    if (!trySetUpClient()) {
      return;
    }
    startClientApp();
  }

  /** Method is used to initialize the default text bundles in english. */
  private void initDefaultBundles() {
    labels = ResourceBundle.getBundle("gui-labels", new Locale(""));
    errors = ResourceBundle.getBundle("error-messages", new Locale(""));
  }

  /** Method is used to initialize the file chooser for the log file directory. */
  private void initFileChooser() {
    fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
  }

  /**
   * Method attempts to set up client connection and interface settings including language, server
   * address and file path directory.
   *
   * @return boolean whether set up was successful
   */
  private boolean trySetUpClient() {
    if (!tryChooseLanguage()) {
      return false;
    }
    if (!tryChooseServerAddress()) {
      return false;
    }
    return tryChooseLogFileDirectory();
  }

  /** Method initializes the client main interface and connects to the input server address. */
  private void startClientApp() {
    try {
      ClientGUI clientGUI = new ClientGUI(locale, serverAddress, logFilePath);
      clientGUI.startApp();
    } catch (IOException e) {
      handleClientIssues();
    }
  }

  /**
   * Method attempts to select a language for the client user interface and returns true if
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
   * Method is used to select a server address. If address is valid method will return true, if
   * invalid the method with recurse, if no output is provided, method will return false.
   *
   * @return a boolean
   */
  private boolean tryChooseServerAddress() {
    serverAddress = null;
    String addressInput =
        JOptionPane.showInputDialog(null, labels.getString("select.address"), "127.0.0.1:8080");
    if (addressInput == null) {
      return false;
    }
    try {
      formatAddressInput(addressInput);
    } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException | NullPointerException e) {
      showError(errors.getString("invalid.address.input"));
      tryChooseServerAddress();
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
                labels.getString("client.frame"),
                JOptionPane.QUESTION_MESSAGE,
                null,
                languages,
                Language.ENGLISH);
  }

  /**
   * Method is used to format the string address input by splitting it into ip address and port
   * number.
   *
   * @param addressInput is a string input
   * @throws NumberFormatException when port number contains non-num chars.
   */
  private void formatAddressInput(String addressInput) throws NumberFormatException {
    String[] separateInput = addressInput.split(":");
    String ipAddress = separateInput[0];
    int port = Integer.parseInt(separateInput[1]);
    serverAddress = new InetSocketAddress(ipAddress, port);
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
   * Method is used to handle connection problems with server. It asks for new server address and
   * then starts the client app if one is provided, otherwise closing the application.
   */
  private void handleClientIssues() {
    serverAddress = null;
    showError(errors.getString("connection.not.possible"));
    tryChooseServerAddress();
    if (serverAddress == null) {
      return;
    }
    startClientApp();
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
