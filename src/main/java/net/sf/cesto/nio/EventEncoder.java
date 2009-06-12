package net.sf.cesto.nio;

import static org.jboss.netty.channel.Channels.write;
import net.sf.cesto.Event;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

@ChannelPipelineCoverage("all")
public class EventEncoder extends SimpleChannelHandler {

  @Override
  public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

    Object message = e.getMessage();
    if (!(message instanceof Event)) {
      // this can only deal with events
      ctx.sendDownstream(e);
      return;
    }
    Event event = (Event) message;

    ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
    byte[] type = event.getType().getBytes();
    byte[] data = event.getData();

    buf.writeInt(type.length + data.length);
    buf.writeInt(type.length);
    buf.writeBytes(type);
    buf.writeBytes(data);
    write(ctx, e.getChannel(), e.getFuture(), buf, e.getRemoteAddress());

  }

}
