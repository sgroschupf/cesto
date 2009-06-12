package net.sf.cesto.nio.server;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.cesto.Event;
import net.sf.cesto.processors.IProcessor;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * Handler for a channel
 */
@ChannelPipelineCoverage("one")
public class EventServerHandler extends SimpleChannelHandler {

  private static final Logger logger = Logger.getLogger(EventServerHandler.class.getName());
  private IProcessor _processor;
  private final AtomicLong transferredMessages = new AtomicLong();

  public EventServerHandler(IProcessor processor) {
    _processor = processor;
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
    transferredMessages.incrementAndGet();
    Event event = (Event) e.getMessage();
    boolean sucess = _processor.process(event);
    e.getChannel().write(new Boolean(sucess));
  }

  @Override
  public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
    logger.info("Client disconnected: " + ctx.getName());
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
    logger.log(Level.WARNING, "Unexpected exception from downstream.", e.getCause());
    e.getChannel().close();
  }

  public long getTransferredMessages() {
    return transferredMessages.get();
  }
}
