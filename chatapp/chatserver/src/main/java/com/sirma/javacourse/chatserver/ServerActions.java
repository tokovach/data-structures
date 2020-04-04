package com.sirma.javacourse.chatserver;

import com.sirma.javacourse.chatcommon.log.LogModel;
import com.sirma.javacourse.chatserver.servergui.ClientListModel;

public interface ServerActions {
  void startServer();

  void stopServer();

  void setServerLogModel(LogModel logModel);

  void setClientListModel(ClientListModel listModel);
}
