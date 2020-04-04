package com.sirma.javacourse.chatserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.sirma.javacourse.chatserver.serverworkers.ClientAcceptor;
import com.sirma.javacourse.chatserver.serverworkers.Connection;
import com.sirma.javacourse.chatserver.serverworkers.IServerConnection;

class ClientAcceptorTest {
  private static ClientAcceptor acceptor;
  private IServerConnection serverConnection;
  private Queue<Connection> connectionQueue;
  private Socket mockedSocket;
  private OutputStream dummyOutputStream =
      new OutputStream() {
        @Override
        public void write(int b) {}
      };
  private InputStream dummyInputStream =
      new InputStream() {
        @Override
        public int read() {
          return 0;
        }
      };

  private void mockSocket() throws IOException {
    mockedSocket = Mockito.mock(Socket.class);
    Mockito.doReturn(dummyOutputStream).when(mockedSocket).getOutputStream();
    Mockito.doReturn(dummyInputStream).when(mockedSocket).getInputStream();
  }

  private void initServerConnection() {
    serverConnection =
        new IServerConnection() {
          @Override
          public Socket accept() {
            return mockedSocket;
          }

          @Override
          public void close() {}
        };
  }

  @Test
  void acceptClient_CheckAcceptNewConnections() throws IOException {
    mockSocket();
    initServerConnection();
    IServerConnection mockedServerConnection = Mockito.spy(serverConnection);
    connectionQueue = new LinkedBlockingQueue<>(3);
    acceptor = new ClientAcceptor(mockedServerConnection, (e) -> connectionQueue.add(e));
    assertThrows(IllegalStateException.class, () -> acceptor.process());
    assertEquals(3, connectionQueue.size());
    Mockito.verify(mockedServerConnection, Mockito.times(4)).accept();
    Mockito.verify(mockedSocket, Mockito.times(4)).getOutputStream();
    Mockito.verify(mockedSocket, Mockito.times(4)).getInputStream();
  }
}
