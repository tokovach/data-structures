package com.sirma.javacourse.chatcommon.log;

import static com.sirma.javacourse.chatcommon.Clock.getCurrentDate;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Log that saves messages in a text file. */
public class Log {
  private static final Logger LOGGER = LoggerFactory.getLogger(Log.class);
  private ResourceBundle exceptions = ResourceBundle.getBundle("log-exceptions");
  private BufferedWriter logFileWriter;
  private LogModel logModel;
  private boolean isModelInitialized = false;

  /**
   * Constructor initializes the directory for the log file.
   *
   * @param path is a path object
   * @throws IOException when there is an issue with provided path
   */
  public Log(Path path) throws IOException {
    initLog(path);
  }

  /**
   * Method is used to set log model which updates the model with all incoming log messages.
   *
   * @param logModel is a log model object
   */
  public void setLogModel(LogModel logModel) {
    this.logModel = logModel;
    this.isModelInitialized = true;
  }

  /**
   * Method adds an input message to the log file and if log model is set also updates it as well.
   *
   * @param message is a string message
   */
  public void addMessage(String message) {
    try {
      logFileWriter.write(message + "\n");
      logFileWriter.flush();
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }
    if (isModelInitialized) {
      logModel.update(message);
    }
  }

  /**
   * Method is used to initialize the log file.
   *
   * @param path is the path of the log file
   * @throws IOException when an issue occurs with the creation of the log text file
   */
  private void initLog(Path path) throws IOException {
    if (!isPathDirectory(path)) {
      throw new IOException(
          String.format(exceptions.getString("invalid.log.file.path"), path.toString()));
    }
    initLogFileWriter(createNewLogFilePath(path));
  }

  /**
   * Method checks if provided path is a directory.
   *
   * @param path is a path object
   * @return boolean
   */
  private boolean isPathDirectory(Path path) {
    return Files.isDirectory(path);
  }

  /**
   * Method initializes the log file buffered writer.
   *
   * @param path is a path object
   * @throws IOException when error occurs initializing the log file writer
   */
  private void initLogFileWriter(Path path) throws IOException {
    logFileWriter =
        Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
  }

  /**
   * Method is used to get log file path and create the name for the log file.
   *
   * @param path is a path object
   * @return a new path with log file name
   */
  private Path createNewLogFilePath(Path path) {
    String currentPath = path.toString();
    String newFileName = "LogFile" + getCurrentDate() + ".txt";
    String newPath = currentPath + "/" + newFileName;
    return Paths.get(newPath);
  }
}
