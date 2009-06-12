package net.sf.cesto;
import java.io.File;

import net.sf.cesto.processors.FileProcessor;

import org.junit.Test;


public class FileProcessorTest extends BaseProcessorTest {

  private static final int ITERATIONS = 100000;
  static final byte[] MSG = "{\"event_uuid\":\"bf3421bb9cb24a26983a290702c87f23\",\"event_timestamp\":1234492967823877,\"event_type\":\"grid-tools-ape-event\"}"
      .getBytes();

  @Test
  public void testProcess() {
    File file = new File("./build/out");
    file.mkdirs();
    FileProcessor fileProcessor = new FileProcessor();
    fileProcessor.setFile(file.getAbsolutePath());
    String[] types = new String[] { "A", "B", "C", "D", "E" };
    writeEvents(fileProcessor, ITERATIONS, types, MSG);

  }
}
