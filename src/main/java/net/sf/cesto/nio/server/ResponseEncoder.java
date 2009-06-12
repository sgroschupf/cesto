package net.sf.cesto.nio.server;

import static org.jboss.netty.channel.Channels.write;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

@ChannelPipelineCoverage("all")
public class ResponseEncoder extends SimpleChannelHandler {

  private static final byte TRUE = (byte) 1;
  private static final byte FALSE = (byte) 0;

  @Override
  public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

    Object message = e.getMessage();
    boolean result = ((Boolean) message).booleanValue();
    ChannelBuffer buf = ChannelBuffers.buffer(1)    ;
    if (result) {
      buf.writeByte(TRUE);
    } else {
      buf.writeByte(FALSE);
    }
    // send
    write(ctx, e.getChannel(), e.getFuture(), buf, e.getRemoteAddress());
  }
}
