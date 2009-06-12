package net.sf.cesto.nio.server;

import static org.jboss.netty.channel.Channels.pipeline;

import net.sf.cesto.processors.IProcessor;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

public class EventServerPipelineFactory implements ChannelPipelineFactory {

  private IProcessor _processor;

  public EventServerPipelineFactory(IProcessor processor) {
    _processor = processor;
  }

  public ChannelPipeline getPipeline() throws Exception {
    ChannelPipeline pipeline = pipeline();

    // codec
    pipeline.addLast("decoder", new EventDecoder());
    pipeline.addLast("encoder", new ResponseEncoder());

    // one handler for each channel since it is stateful.
    EventServerHandler handler = new EventServerHandler(_processor);
    pipeline.addLast("handler", handler);
    new ThroughputMonitor(handler).start();
    return pipeline;
  }
}
