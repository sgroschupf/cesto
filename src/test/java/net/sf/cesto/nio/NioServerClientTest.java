package net.sf.cesto.nio;

import net.sf.cesto.BaseProcessorTest;
import net.sf.cesto.nio.server.NioServer;
import net.sf.cesto.processors.IProcessor;
import net.sf.cesto.processors.NullProcessor;

import org.junit.Test;

public class NioServerClientTest extends BaseProcessorTest {

  private static final int ITERATIONS = 100000;
  static final byte[] MSG = "{\"event_uuid\":\"bf3421bb9cb24a26983a290702c87f23\",\"event_timestamp\":1234492967823877,\"event_type\":\"grid-tools-ape-event\"}"
      .getBytes();
  private static final int THREADS = 50;

  @Test
  public void testServerClient() throws Exception {
    NioServer nioServer = new NioServer(9999, new NullProcessor());
    EventClient eventClient = new EventClient("127.0.0.1", 9999);

    writeEvents(eventClient, ITERATIONS, new String[] { "A", "B", "C" }, MSG);

    new Worker(eventClient).start();
    new Worker(eventClient).start();
    new Worker(eventClient).start();
    new Worker(eventClient).start();
    new Worker(eventClient).start();
   
  }

  class Worker extends Thread {
    private IProcessor eventClient;

    public Worker(IProcessor p) {
      eventClient = p;
    }

    @Override
    public void run() {
      writeEvents(eventClient, ITERATIONS, new String[] { "A", "B", "C" }, MSG);
    }

  }

  // @Test
  // public void testMultThreaded() throws IOException, InterruptedException {
  // NioServer nioServer = new NioServer(9999, new NullProcessor());
  //
  // String[] types = new String[] { "1", "2", "3" };
  // ClientThread[] clientThreads = new ClientThread[THREADS];
  //
  // for (int i = 0; i < clientThreads.length; i++) {
  // clientThreads[i] = new ClientThread(10000, MSG, types);
  // clientThreads[i].start();
  // }
  //
  // long time = 0;
  // long msgCount = 0;
  // long bytesCount = 0;
  // for (int i = 0; i < clientThreads.length; i++) {
  // if (clientThreads[i].isAlive()) {
  // clientThreads[i].join();
  // }
  // time += clientThreads[i]._took;
  // msgCount += clientThreads[i]._messages;
  // System.out.println(clientThreads[i]._messages / clientThreads[i]._took);
  // bytesCount += clientThreads[i]._bytes;
  // }
  //
  // // stats
  // long avTime = time / THREADS;
  //
  // System.out.println("Messages send: " + msgCount);
  // System.out.println("took average: " + avTime);
  // System.out.println("throughput average: " + ((float) msgCount / THREADS) /
  // avTime);
  // System.out.println("throughput average: " + (((float) bytesCount / THREADS)
  // / 1024) / ((float) avTime / 1000)
  // + " kbytes / s");
  //
  // }
  //
  // class ClientThread extends Thread {
  //
  // private EventClient _processor;
  // private int _iterations;
  // private String[] _types;
  // private byte[] _msg;
  // private int _bytes;
  // private long _took;
  // private int _messages;
  //
  // public ClientThread(int iterations, byte[] msg, String[] types) {
  // _iterations = iterations;
  // _msg = msg;
  // _types = types;
  //
  // _processor = new EventClient("localhost", 9999);
  // }
  //
  // @Override
  // public void run() {
  // long start = System.currentTimeMillis();
  // for (int i = 0; i < _iterations; i++) {
  // for (String type : _types) {
  // _processor.process(new Event(type, _msg));
  // _bytes += type.getBytes().length + _msg.length;
  // }
  // }
  // _took = System.currentTimeMillis() - start;
  // _messages = _iterations * _types.length;
  // }
  //
  // }
}
