package net.sf.cesto.processors;

import net.sf.cesto.Event;

public abstract class BaseProcessor implements IProcessor {
  private IProcessor _processor;

  public void setSuccessor(IProcessor p) {
    _processor = p;

  }

  public IProcessor getSuccessor() {
    return _processor;
  }

  public abstract boolean process(Event event);

}
