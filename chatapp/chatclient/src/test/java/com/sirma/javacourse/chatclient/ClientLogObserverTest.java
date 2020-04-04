package com.sirma.javacourse.chatclient;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.log.ClientLogObserver;
import com.sirma.javacourse.chatcommon.log.Log;

class ClientLogObserverTest {
  private Log log = Mockito.mock(Log.class);
  private List<String> users = new ArrayList<>();
  private String user = "test123";
  private Message addMessage =
      Message.saveMessage(ActionType.CLIENT_JOIN_MESSAGE).username(user).build();
  private Message invalidMessage =
      Message.saveMessage(ActionType.CLIENT_LIST).clientList(users).build();
  private Message invalidMessage1 =
      Message.saveMessage(ActionType.UNAVAILABLE_USERNAME).username(user).build();
  private Message invalidMessage2 =
      Message.saveMessage(ActionType.INVALID_FORMAT_USERNAME).username(user).build();
  private Message invalidMessage3 =
          Message.saveMessage(ActionType.MESSAGE_RECEIVER_START).username(user).build();


  @Test
  void update_ValidMessage() {
    Mockito.doNothing().when(log).addMessage(any());
    ClientLogObserver logObserver = new ClientLogObserver(log,new Locale(""));
    logObserver.update(addMessage);
    Mockito.verify(log,Mockito.times(1)).addMessage(any());
  }


  @Test
  void update_InvalidMessage() {
    Mockito.doNothing().when(log).addMessage(any());
    ClientLogObserver logObserver = new ClientLogObserver(log,new Locale(""));
    logObserver.update(invalidMessage);
    logObserver.update(invalidMessage1);
    logObserver.update(invalidMessage2);
    logObserver.update(invalidMessage3);
    Mockito.verify(log,Mockito.times(0)).addMessage(any());
  }
}
