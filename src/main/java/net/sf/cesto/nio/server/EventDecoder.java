package net.sf.cesto.nio.server;

import net.sf.cesto.Event;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class EventDecoder extends FrameDecoder {

  @Override
  protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
    // wait for prefix
    if (buffer.readableBytes() < 4) {
      return null;
    }

    int length = buffer.getInt(buffer.readerIndex());
    // wait for the complete event
    if (buffer.readableBytes() < length + 4) {
      return null;
    }

    // skip package length header
    buffer.skipBytes(4);
    // decode
    int typeLength = buffer.readInt();
    String type = buffer.toString(buffer.readerIndex(), typeLength, "UTF-8");
    int eventLength = length - typeLength;
    byte[] eventBytes = new byte[eventLength];
    buffer.skipBytes(typeLength);
    buffer.readBytes(eventBytes);
    return new Event(type, eventBytes);
  }

  // @Override
  // public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
  // throws Exception {
  // super.exceptionCaught(ctx, e);
  // throw new RuntimeException(e.getCause());
  // }
}
