package net.sf.cesto;
import java.io.File;

import junit.framework.Assert;

import net.sf.cesto.ProcessorFactory;
import net.sf.cesto.processors.FileProcessor;
import net.sf.cesto.processors.IProcessor;

import org.junit.Test;


public class ProcessorFactoryTest {

  @Test
  public void testCreateProcessor() {
    File file = new File("./build/test/output");
    String s = "FileProcessor(file:" + file.getAbsolutePath() +")";
    FileProcessor p = (FileProcessor) ProcessorFactory.createProcessorFromString(s);
    Assert.assertNotNull(p);
    Assert.assertEquals(p.getClass(), FileProcessor.class);
    Assert.assertEquals(p.getFile().getAbsolutePath(), file.getAbsolutePath());
  }

  @Test
  public void testGetPipe() {
    String s = "NullProcessor,NullProcessor";
    IProcessor creatPipeFromString = ProcessorFactory.creatPipeFromString(s);
    Assert.assertNotNull(creatPipeFromString.getSuccessor());
  }

}
