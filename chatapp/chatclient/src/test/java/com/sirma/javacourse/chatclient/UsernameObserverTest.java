package com.sirma.javacourse.chatclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.Test;

import com.sirma.javacourse.chatclient.clientworkers.ActionType;
import com.sirma.javacourse.chatclient.clientworkers.Message;
import com.sirma.javacourse.chatclient.clientworkers.UsernameObserver;

class UsernameObserverTest {
  private Queue<ActionType> acceptedChangeUsername;
  private UsernameObserver observer = new UsernameObserver(this::add);
  private Message message1 =
          Message.saveMessage(ActionType.CLIENT_JOIN_MESSAGE).username("Test").build();
  private Message message2 =
          Message.saveMessage(ActionType.CLIENT_LEAVE_MESSAGE).username("Test").build();
  private Message message3 =
          Message.saveMessage(ActionType.CLIENT_LIST).username("Test").build();
  private Message changeMessage1 =
          Message.saveMessage(ActionType.UNAVAILABLE_USERNAME).username("Test").build();
  private Message changeMessage2 =
          Message.saveMessage(ActionType.INVALID_FORMAT_USERNAME).username("Test").build();



  private void add(ActionType actionType) {
    acceptedChangeUsername.add(actionType);
  }

  @Test
  void update_NoChanges() {
    acceptedChangeUsername = new LinkedList<>();
    observer.update(message1);
    observer.update(message2);
    observer.update(message3);
    assertTrue(acceptedChangeUsername.isEmpty());
  }


  @Test
  void update_Change_InvalidFormat() {
    acceptedChangeUsername = new LinkedList<>();
    observer.update(changeMessage2);
    assertFalse(acceptedChangeUsername.isEmpty());
    assertEquals(ActionType.INVALID_FORMAT_USERNAME,acceptedChangeUsername.poll());
  }

  @Test
  void update_Change_UnavailableUsername() {
    acceptedChangeUsername = new LinkedList<>();
    observer.update(changeMessage1);
    assertFalse(acceptedChangeUsername.isEmpty());
    assertEquals(ActionType.UNAVAILABLE_USERNAME,acceptedChangeUsername.poll());
  }
}
