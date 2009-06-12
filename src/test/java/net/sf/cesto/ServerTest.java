package net.sf.cesto;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import net.sf.cesto.Server;
import net.sf.cesto.processors.RelayProcessor;

import org.junit.Test;


public class ServerTest extends BaseProcessorTest {

  private static final int ITERATIONS = 100000;
  static final byte[] MSG = "{\"event_uuid\":\"bf3421bb9cb24a26983a290702c87f23\",\"event_timestamp\":1234492967823877,\"event_type\":\"grid-tools-ape-event\"}"
      .getBytes();
  private static final int THREADS = 50;

  @Test
  public void testWriteToServer() throws Exception {
    File file = new File("./build/out");
    Properties p = new Properties();
    // p.put("1", "FileProcessor(file:" + file.getAbsolutePath() + ")");
    // p.put("2", "FileProcessor(file:" + file.getAbsolutePath() + ")");
    // p.put("3", "FileProcessor(file:" + file.getAbsolutePath() + ")");

    p.put("1", "NullProcessor()");
    p.put("2", "NullProcessor()");
    p.put("3", "NullProcessor()");

    Server server = new Server(9998, p);
    server.start();

    RelayProcessor processor = new RelayProcessor();
    processor.setHost("localhost");
    processor.setPort(9999);
    processor.start();

    String[] types = new String[] { "1", "2", "3" };
    writeEvents(processor, ITERATIONS, types, MSG);
    server.stop();

  }

//  @Test
//  public void testMultThreaded() throws IOException, InterruptedException {
//    Properties p = new Properties();
//    p.put("1", "NullProcessor()");
//    p.put("2", "NullProcessor()");
//    p.put("3", "NullProcessor()");
//
//    Server server = new Server(9999, p);
//    server.start();
//    String[] types = new String[] { "1", "2", "3" };
//    ClientThread[] clientThreads = new ClientThread[THREADS];
//
//    for (int i = 0; i < clientThreads.length; i++) {
//      clientThreads[i] = new ClientThread(10000, MSG, types);
//      clientThreads[i].start();
//    }
//
//    long time = 0;
//    long msgCount = 0;
//    long bytesCount = 0;
//    for (int i = 0; i < clientThreads.length; i++) {
//      if (clientThreads[i].isAlive()) {
//        clientThreads[i].join();
//      }
//      time += clientThreads[i]._took;
//      msgCount += clientThreads[i]._messages;
//      System.out.println(clientThreads[i]._messages/clientThreads[i]._took);
//      bytesCount += clientThreads[i]._bytes;
//    }
//
//    // stats
//    long avTime = time / THREADS;
//
//    System.out.println("Messages send: " + msgCount);
//    System.out.println("took average: " + avTime);
//    System.out.println("throughput average: " + ((float) msgCount / THREADS) / avTime);
//    System.out.println("throughput average: " + (((float) bytesCount / THREADS)/1024) / ((float)avTime/1000)+" kbytes / s");
//
//  }
//
//  class ClientThread extends Thread {
//
//    private RelayProcessor _processor;
//    private int _iterations;
//    private String[] _types;
//    private byte[] _msg;
//    private int _bytes;
//    private long _took;
//    private int _messages;
//
//    public ClientThread(int iterations, byte[] msg, String[] types) {
//      _iterations = iterations;
//      _msg = msg;
//      _types = types;
//
//      _processor = new RelayProcessor();
//      _processor.setHost("localhost");
//      _processor.setPort(9999);
//      _processor.start();
//    }
//
//    @Override
//    public void run() {
//      long start = System.currentTimeMillis();
//      for (int i = 0; i < _iterations; i++) {
//        for (String type : _types) {
//          _processor.process(type, _msg);
//          _bytes += type.getBytes().length + _msg.length;
//        }
//      }
//      _took = System.currentTimeMillis() - start;
//      _messages = _iterations * _types.length;
//    }
//
//  }
}
