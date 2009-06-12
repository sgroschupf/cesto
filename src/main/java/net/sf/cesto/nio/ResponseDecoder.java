package net.sf.cesto.nio;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class ResponseDecoder extends FrameDecoder {
  private static final byte TRUE = (byte) 1;

  @Override
  protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
    if (buffer.readableBytes() < 1) {
      return null;
    }
    byte response = buffer.readByte();
    return new Boolean(response == TRUE); // everything else is wrong
  }

}
