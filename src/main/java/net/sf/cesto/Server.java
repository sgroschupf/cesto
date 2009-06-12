package net.sf.cesto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.cesto.processors.IProcessor;

public class Server extends Thread {

  private ServerSocket _socket;
  private HashMap<String, IProcessor> _processor = new HashMap<String, IProcessor>();

  public Server(int port, Properties properties) throws IOException {
    Set<Entry<Object, Object>> entrySet = properties.entrySet();
    for (Entry<Object, Object> entry : entrySet) {
      String topic = (String) entry.getKey();
      String value = (String) entry.getValue();
      IProcessor processor = ProcessorFactory.creatPipeFromString(value);
      _processor.put(topic, processor);
    }

    _socket = new ServerSocket(port);
  }

  @Override
  public void run() {
    while (!isInterrupted()) {
      try {
        Socket socket = _socket.accept();
        new ClientConnection(socket).start();
      } catch (IOException e) {
        // TODO for sure we dont want to systou
        System.out.println("Unable to establish client connetion:" + e.toString());
      }
    }
  }

  class ClientConnection extends Thread {
    private final Socket _clientSocket;

    public ClientConnection(Socket socket) {
      _clientSocket = socket;
    }

    public void run() {
      try {
        BufferedInputStream inputStream = new BufferedInputStream(_clientSocket.getInputStream());
        BufferedOutputStream outputStream = new BufferedOutputStream(_clientSocket.getOutputStream());
        while (true) {
          int typeCount = inputStream.read();
          byte[] typeBytes = new byte[typeCount];
          inputStream.read(typeBytes);
          String type = new String(typeBytes);

          int msgCount = inputStream.read();
          byte[] msgBytes = new byte[msgCount];
          inputStream.read(msgBytes);

          IProcessor processor = _processor.get(type);
          // synchronized (processor) {
          boolean process = processor.process(new Event(type, msgBytes));
          if (process) {
            outputStream.write(1);
          } else {
            outputStream.write(0);
          }
          outputStream.flush();
          // }

        }
      } catch (IOException e) {
        // TODO
        System.out.println("Unable to read from client connetion:" + e.toString());
      }
    }
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    String usage = "Usage: Server <port>";
    if (args.length != 1) {
      System.out.println(usage);
      System.exit(-1);
    }
    InputStream resourceAsStream = Server.class.getResourceAsStream("/del.properties");
    Properties properties = new Properties();
    properties.load(resourceAsStream);
    Server server = new Server(Integer.parseInt(args[0]), properties);
    server.start();
    server.join();
  }

}
