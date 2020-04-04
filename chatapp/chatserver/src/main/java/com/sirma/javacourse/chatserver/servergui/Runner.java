package com.sirma.javacourse.chatserver.servergui;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Runner {
  public static void main(String[] args) throws UnknownHostException {
  System.out.println(InetAddress.getLocalHost());
	StartUpDialog start = new StartUpDialog();
	start.startApp();
  }
}
