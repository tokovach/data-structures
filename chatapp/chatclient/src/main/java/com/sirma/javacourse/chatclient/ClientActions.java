package com.sirma.javacourse.chatclient;

import com.sirma.javacourse.chatclient.clientgui.ClientListModel;
import com.sirma.javacourse.chatcommon.log.LogModel;

public interface ClientActions {
  void connect();

	void disconnect();

  void sendMessage(String message);

  void setListModel(ClientListModel listModel);

  void setLogModel(LogModel logModel);
}
