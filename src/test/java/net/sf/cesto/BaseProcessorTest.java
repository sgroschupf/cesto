package net.sf.cesto;

import net.sf.cesto.processors.IProcessor;

public class BaseProcessorTest {

  protected void writeEvents(IProcessor processor, int iterations, String[] types, byte[] msg) {
    long bytes = 0;
    long start = System.currentTimeMillis();
    for (int i = 0; i < iterations; i++) {
      for (String type : types) {
        processor.process(new Event(type, msg));
        bytes += type.getBytes().length + msg.length;
      }
    }
    long took = System.currentTimeMillis() - start;
    int messages = iterations * types.length;
    System.out.println("written messages: " + messages);
    System.out.println("took: " + took + " ms");
    System.out.println("throughput: " + (messages / (float) took) + " msg per ms");
    System.out.println("throughput: " + (messages / (float) (took / 1000)) + " msg per s");
    System.out.println("throughput: " + ((bytes / 1024f) / (float) (took / 1000)) + " kbytes per s");
  }

}
