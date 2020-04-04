package com.sirma.javacourse.chatserver.servergui;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sirma.javacourse.chatcommon.StatusField;
import com.sirma.javacourse.chatcommon.log.LogModel;
import com.sirma.javacourse.chatserver.Server;
import com.sirma.javacourse.chatserver.ServerActions;

/**
 * A graphical interface class that shows a window with a log, client list and controls for starting
 * and stopping server.
 */
public class ServerGUI {
  private static final Logger LOGGER = LoggerFactory.getLogger(ServerGUI.class);
  private ResourceBundle labels;
  private JFrame frame;
  private JPanel logPanel;
  private JPanel clientListPanel;
  private JPanel buttonPanel;
  private JPanel toolPanel;
  private JPanel mainPanel;
  private JButton startServerButton;
  private JButton stopServerButton;
  private StatusField logStatusField;
  private StatusField clientListField;
  private Locale locale;
  private ServerActions serverActions;

  /**
   * Constructor is used to initialize the language of the interface, port number and log file path.
   *
   * @param locale is the language of the interface
   * @param port is an integer port number
   * @param logFilePath is the log file path object
   * @throws IOException when the selected port is taken or the log file path is invalid
   */
  public ServerGUI(Locale locale, int port, Path logFilePath) throws IOException {
    this.locale = locale;
    initLabels(locale);
    initServer(port, logFilePath);
    initMainWindow();
    initSubWindows();
    initStartServerControl();
    initStopServerControl();
    initLogField();
    initClientListField();
    initLogModel();
    initClientListModel();
    initStartControlListener();
    initStopControlListener();
    setSubWindowLayout();
  }

  /** Method is used to start the application. */
  public void startApp() {
    frame.getContentPane().add(mainPanel);
    frame.setVisible(true);
    frame.setLocationRelativeTo(null);
  }

  /** Method is used to close the application and stop the server. */
  public void closeApp() {
    serverActions.stopServer();
    SwingUtilities.invokeLater(() -> frame.dispose());
  }

  /**
   * Method initializes the server.
   *
   * @param port is an integer port number
   * @param logFilePath is a path object
   * @throws IOException when the selected port is taken or the log file path is invalid
   */
  private void initServer(int port, Path logFilePath) throws IOException {
    serverActions = new Server(locale, port, logFilePath);
  }

  /**
   * Method is used to initialize the labels for the user interface.
   *
   * @param locale is the language in which we want to see the labels
   */
  private void initLabels(Locale locale) {
    labels = ResourceBundle.getBundle("gui-labels", locale);
  }

  /** Method is used to initialize the main window where all sub-windows are added. */
  private void initMainWindow() {
    frame = new JFrame(labels.getString("server.frame"));
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException
        | InstantiationException
        | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      LOGGER.error(e.getMessage());
    }
    frame.setSize(900, 400);
    frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  /** Method initializes the panel windows where the status fields and the controls are added. */
  private void initSubWindows() {
    logPanel = new JPanel();
    clientListPanel = new JPanel();
    buttonPanel = new JPanel();
    toolPanel = new JPanel();
    mainPanel = new JPanel();
  }

  /** Method initializes the start server control. */
  private void initStartServerControl() {
    startServerButton = new JButton(labels.getString("start.control"));
    buttonPanel.add(startServerButton);
  }

  /** Method initializes the stop server control. */
  private void initStopServerControl() {
    stopServerButton = new JButton(labels.getString("stop.control"));
    buttonPanel.add(stopServerButton);
  }

  /** Method is used to initialize the log field where messages are displayed. */
  private void initLogField() {
    JTextArea statusInformationTextArea = new JTextArea();
    statusInformationTextArea.setEditable(false);
    statusInformationTextArea.setLineWrap(true);
    JScrollPane scrollPane = new JScrollPane(statusInformationTextArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    DefaultCaret caret = (DefaultCaret) statusInformationTextArea.getCaret();
    caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
    logPanel.add(scrollPane);
    logStatusField = new StatusField(statusInformationTextArea);
  }

  /** Method is used to initialize the client list field. */
  private void initClientListField() {
    JTextArea statusInformationTextArea = new JTextArea();
    statusInformationTextArea.setEditable(false);
    statusInformationTextArea.setLineWrap(true);
    JScrollPane scrollPane = new JScrollPane(statusInformationTextArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    DefaultCaret caret = (DefaultCaret) statusInformationTextArea.getCaret();
    caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
    clientListPanel.add(scrollPane);
    clientListField = new StatusField(statusInformationTextArea);
  }

  /**
   * Method is used to initialize the log model which updates the incoming messages to the log
   * field.
   */
  private void initLogModel() {
    LogModel logModel = new LogModel(logStatusField);
    serverActions.setServerLogModel(logModel);
  }

  /** Method is used to initialize the list model which updates the client list. */
  private void initClientListModel() {
    ClientListModel listModel = new ClientListModel(clientListField);
    serverActions.setClientListModel(listModel);
  }

  /** Method is used to initialize the listener for the start server control. */
  private void initStartControlListener() {
    startServerButton.addActionListener(
        (event) -> {
          serverActions.startServer();
          startServerButton.setEnabled(false);
        });
  }

  /** Method is used to initialize the listener for the stop server control. */
  private void initStopControlListener() {
    stopServerButton.addActionListener(
        (e) -> {
          serverActions.stopServer();
          closeApp();
        });
  }

  /** Method is used to set the layout of all sub windows */
  private void setSubWindowLayout() {
    Border logBorder = BorderFactory.createTitledBorder(labels.getString("log.border"));
    Border clientListBorder = BorderFactory.createTitledBorder(labels.getString("list.border"));
    buttonPanel.setLayout(new GridLayout(2, 1));
    logPanel.setLayout(new GridLayout(1, 1));
    logPanel.setBorder(logBorder);
    clientListPanel.setLayout(new GridLayout(1, 1));
    clientListPanel.setBorder(clientListBorder);
    toolPanel.setLayout(new GridLayout(1, 2));
    mainPanel.setLayout(new GridLayout(1, 2));
    toolPanel.add(clientListPanel);
    toolPanel.add(buttonPanel);
    mainPanel.add(logPanel);
    mainPanel.add(toolPanel);
  }
}
