package net.sf.cesto.processors;

import net.sf.cesto.Event;

/**
 * like writing to /dev/null
 * 
 */
public class NullProcessor extends BaseProcessor {

  @Override
  public boolean process(Event event) {
    return true;
  }

}
