package net.sf.cesto.processors;

import net.sf.cesto.Event;

public interface IProcessor {

  void setSuccessor(IProcessor p);

  IProcessor getSuccessor();

  boolean process(Event event);
  
}
