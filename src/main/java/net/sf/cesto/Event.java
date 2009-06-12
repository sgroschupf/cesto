package net.sf.cesto;

import java.io.ByteArrayOutputStream;

public class Event {

  private String _type;
  private byte[] _data;

  public Event(String type, byte[] data) {
    _type = type;
    _data = data;
  }

  public String getType() {
    return _type;
  }

  public byte[] getData() {
    return _data;
  }

 
}
