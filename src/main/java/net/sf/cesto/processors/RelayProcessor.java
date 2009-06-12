package net.sf.cesto.processors;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import net.sf.cesto.Event;

public class RelayProcessor extends BaseProcessor implements Startable {

  private String _host;
  private int _port;
  private Socket _socket;
  private BufferedOutputStream _outputStream;
  private BufferedInputStream _inputStream;

  public void setHost(String host) {
    _host = host;
  }

  public void setPort(int port) {
    _port = port;
  }

  public void start() {
    try {
      _socket = new Socket(_host, _port);
      _outputStream = new BufferedOutputStream(_socket.getOutputStream(), 1024);
      _inputStream = new BufferedInputStream(_socket.getInputStream());
    } catch (Exception e) {
      throw new RuntimeException("Unable to extablish Server connection.", e);
    }

  }

  @Override
  public boolean process(Event event) {
    byte[] typeBytes =event.getType().getBytes();
    try {

      _outputStream.write(typeBytes.length);
      _outputStream.write(typeBytes);

      _outputStream.write(event.getData().length);
      _outputStream.write(event.getData());
      _outputStream.flush();

      int read = _inputStream.read();
      if (read == 0) {
        return false;
      } else if (read == 1) {
        return true;
      }
      // unknown result
      return false;
    } catch (IOException e) {
      // TODO how to write Errors?
      return false;
    }

  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    if (_socket != null) {
      _socket.close();
    }
  }
}
