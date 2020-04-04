package com.sirma.javacourse.chatclient.clientgui;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sirma.javacourse.chatclient.Client;
import com.sirma.javacourse.chatclient.ClientActions;
import com.sirma.javacourse.chatcommon.StatusField;
import com.sirma.javacourse.chatcommon.log.LogModel;

/**
 * A graphical interface class that shows a window with a log, client list and a field for writing
 * messages to other clients.
 */
public class ClientGUI {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientGUI.class);
  private ResourceBundle labels;
  private JFrame frame;
  private JPanel logPanel;
  private JPanel clientListPanel;
  private JPanel messageInputPanel;
  private JPanel buttonPanel;
  private JPanel toolPanel;
  private JPanel mainPanel;
  private JButton sendMessageButton;
  private JButton disconnectButton;
  private JTextField messageInputTextArea;
  private StatusField logStatusField;
  private StatusField clientListField;
  private Locale locale;
  private ClientActions clientActions;

  /**
   * Constructor is used to initialize the user interface and initialize connection to server.
   *
   * @param locale is the language in which we want to see the interface
   * @param serverAddress is the address of the server
   * @param logFilePath is the directory to the log file where we want to save previous messages
   * @throws IOException when client cannot connect to the input server address
   */
  public ClientGUI(Locale locale, InetSocketAddress serverAddress, Path logFilePath)
      throws IOException {
    this.locale = locale;
    initLabels(locale);
    initClient(serverAddress, logFilePath);
    initMainWindow();
    initSubWindows();
    initSendMessageControl();
    initDisconnectControl();
    initMessageInputField();
    initLogField();
    initClientListField();
    initLogModel();
    initClientListModel();
    initMessageControlListener();
    initDisconnectControlListener();
    setSubWindowLayout();
  }

  /** Method connects to the client to the server. */
  public void startApp() {
    clientActions.connect();
  }

  /** Method is used to close the application and connection to server. */
  public void closeApp() {
    clientActions.disconnect();
    SwingUtilities.invokeLater(() -> frame.dispose());
  }

  /** Method is used to show the user interface. */
  private void showApp() {
    frame.getContentPane().add(mainPanel);
    frame.setVisible(true);
    frame.setLocationRelativeTo(null);
  }

  /**
   * Method initializes the client and connects it to the input server address.
   *
   * @param serverAddress is the address of the server we want to connect to
   * @param logFilePath is the directory where we want to save the log from communication with
   *     client and server
   * @throws IOException
   */
  private void initClient(InetSocketAddress serverAddress, Path logFilePath) throws IOException {
    clientActions =
        new Client(serverAddress, logFilePath, locale, this::chooseUsername, (b) -> showApp());
  }

  /**
   * Method is used to initialize the labels for the user interface.
   *
   * @param locale is the language in which we want to see the labels
   */
  private void initLabels(Locale locale) {
    labels = ResourceBundle.getBundle("gui-labels", locale);
  }

  /** Method is used to initialize the main window where all sub-windows are added.*/
  private void initMainWindow() {
    frame = new JFrame(labels.getString("client.frame"));
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException
        | InstantiationException
        | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      LOGGER.error(e.getMessage());
    }
    frame.setSize(750, 400);
    frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  /** Method initializes the panel windows where the status fields and the controls are added. */
  private void initSubWindows() {
    logPanel = new JPanel();
    clientListPanel = new JPanel();
    messageInputPanel = new JPanel();
    buttonPanel = new JPanel();
    toolPanel = new JPanel();
    mainPanel = new JPanel();
  }

  /** Method is used to initialize the send message control. */
  private void initSendMessageControl() {
    sendMessageButton = new JButton(labels.getString("send.message"));
    buttonPanel.add(sendMessageButton);
  }

  /** Method is used to initialize the disconnect control. */
  private void initDisconnectControl() {
    disconnectButton = new JButton(labels.getString("stop.control"));
    buttonPanel.add(disconnectButton);
  }

  /** Method is used to initialize message input field. */
  private void initMessageInputField() {
    messageInputTextArea = new JTextField(200);
    messageInputTextArea.setEnabled(true);
    messageInputPanel.add(messageInputTextArea);
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
    clientActions.setLogModel(logModel);
  }

  /** Method is used to initialize the list model which updates the client list. */
  private void initClientListModel() {
    ClientListModel listModel = new ClientListModel(clientListField);
    clientActions.setListModel(listModel);
  }

  /** Method is used to initialize the listener for the send message control. */
  private void initMessageControlListener() {
    sendMessageButton.addActionListener(
        (event) -> {
          String message = messageInputTextArea.getText();
					if (!message.isEmpty()) {
            clientActions.sendMessage(message);
            messageInputTextArea.setText("");
          }
        });
  }

  /** Method is used to initialize the listener for the disconnect control. */
  private void initDisconnectControlListener() {
    disconnectButton.addActionListener((e) -> closeApp());
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
    messageInputPanel.setLayout(new GridLayout(1, 1));
    toolPanel.setLayout(new GridLayout(1, 3));
    mainPanel.setLayout(new GridLayout(2, 1));
    toolPanel.add(messageInputPanel);
    toolPanel.add(buttonPanel);
    toolPanel.add(clientListPanel);
    mainPanel.add(logPanel);
    mainPanel.add(toolPanel);
  }

  /**
   * Method is used to choose a new username for client. The reason for the picking is provided by
   * string input.
   *
   * @param reason is a string message
   * @return a new username
   */
  private String chooseUsername(String reason) {
    String usernameInput;
    if (reason.contains("SEND")) {
      usernameInput = selectUsername("select.send.username");
    } else if (reason.contains("UNAVAILABLE")) {
      usernameInput = selectUsername("select.unavailable.username");
    } else {
      usernameInput = selectUsername("select.invalid.username");
    }
    return usernameInput;
  }

  /**
   * Method is used to open an option panel for username input by client. The input key label is
   * used to show a message to client about why they have to choose one.
   *
   * @param keyLabel is a string key label
   * @return a string username
   */
  private String selectUsername(String keyLabel) {
    return JOptionPane.showInputDialog(null, labels.getString(keyLabel), "user");
  }
}
