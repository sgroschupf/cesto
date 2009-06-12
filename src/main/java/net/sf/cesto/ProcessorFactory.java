package net.sf.cesto;
import java.lang.reflect.Method;

import net.sf.cesto.processors.IProcessor;
import net.sf.cesto.processors.Startable;


public class ProcessorFactory {

  public static IProcessor creatPipeFromString(String value) {
    String[] strings = value.split(",");
    IProcessor processor = null;
    for (String string : strings) {
      IProcessor p = createProcessorFromString(string);
      if (processor == null) {
        processor = p;
      } else {
        processor.setSuccessor(p);
      }
    }
    return processor;
  }

  public static IProcessor createProcessorFromString(String string) {
    assert string != null;
    String className = getClassName(string);
    IProcessor processor = createInstance(className);
    String[][] parameters = getParameter(className.length() + 1, string);
    if (parameters != null) {
      configureProcessor(processor, parameters);
    }
    return processor;
  }

  private static String[][] getParameter(int start, String string) {
    // no parameter?
    if (start >= string.length()) {
      return null;
    }
    // removing ( and )
    string = string.substring(start, string.length() - 1);
    if(string.length()==0){
      // no config parameter
      return new String[0][];
    }
    String[] split = string.split(" ");
    String[][] parameter = new String[split.length][];
    for (int i = 0; i < split.length; i++) {
      parameter[i] = split[i].split(":");
    }
    return parameter;
  }

  @SuppressWarnings("unchecked")
  private static IProcessor createInstance(String className) {
    Class<IProcessor> loadClass = null;
    // we ignore that and add the standard package
    ClassLoader classLoader = ProcessorFactory.class.getClassLoader();
    try {
      loadClass = (Class<IProcessor>) classLoader.loadClass(className);
    } catch (ClassNotFoundException e) {
    }
    className = "net.sf.cesto.processors." + className;
    try {
      loadClass = (Class<IProcessor>) classLoader.loadClass(className);
    } catch (ClassNotFoundException e) {
    }
    if (loadClass == null) {
      throw new RuntimeException("Unable to find class: " + className);
    }
    IProcessor processor;
    try {
      processor = loadClass.newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Unable to create Processor instance", e);
    }
    return processor;
  }

  private static String getClassName(String string) {
    int nameEnd = string.indexOf("(");
    String className = null;
    if (nameEnd == -1) {
      className = string;
    } else {
      className = string.substring(0, nameEnd);
    }
    return className;
  }

  private static void configureProcessor(IProcessor processor, String[][] parameters) {
    for (String[] parameter : parameters) {
      String key = parameter[0];
      String value = parameter[1];
      Method[] methods = processor.getClass().getMethods();
      for (Method method : methods) {
        if (method.getName().equalsIgnoreCase("set" + key)) {
          try {
            if (!method.getParameterTypes()[0].isAssignableFrom(String.class)) {
              method.invoke(processor, Integer.parseInt(value));
            } else {
              method.invoke(processor, value);
            }
          } catch (Exception e) {
            throw new RuntimeException("Unable to configure processor", e);
          }
        }
      }
      if (processor.getClass().isAssignableFrom(Startable.class)) {
        ((Startable) processor).start();
      }
    }

  }
}
