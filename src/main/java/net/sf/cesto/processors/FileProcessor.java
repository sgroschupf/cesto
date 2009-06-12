package net.sf.cesto.processors;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import net.sf.cesto.Event;

public class FileProcessor extends BaseProcessor {

  private String _path;
  private HashMap<String, BufferedOutputStream> _streams = new HashMap<String, BufferedOutputStream>();
  private static byte[] LINE_BREAK = "\n".getBytes();

  public void setFile(String path) {
    _path = path;
  }

  public File getFile() {
    return new File(_path);
  }

  public boolean process(Event event) {
    try {
      OutputStream outputStream = getStream(event.getType());
      outputStream.write(event.getData());
      outputStream.write(LINE_BREAK);
      outputStream.flush();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private BufferedOutputStream getStream(String type) {
    BufferedOutputStream outputStream = _streams.get(type);
    if (outputStream == null) {
      synchronized (_streams) {
        // this is the slow but reliable section, we have to check again :(
        outputStream = _streams.get(type);
        if (outputStream == null) {
          try {
            outputStream = new BufferedOutputStream(new FileOutputStream(new File(_path, type)));
            _streams.put(type, outputStream);
          } catch (FileNotFoundException e) {
            throw new RuntimeException("unable to open file");
          }
        }
      }
    }
    return outputStream;
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    for (BufferedOutputStream stream : _streams.values()) {
      stream.close();
    }
  }
}
