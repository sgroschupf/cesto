package net.sf.cesto.nio;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import net.sf.cesto.Event;
import net.sf.cesto.processors.BaseProcessor;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class EventClient extends BaseProcessor {

  private Channel _channel;

  public EventClient(String host, int port) {

    ChannelFactory factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors
        .newCachedThreadPool());

    ClientBootstrap bootstrap = new ClientBootstrap(factory);

    bootstrap.setPipelineFactory(new EventClientPipelineFactory());
    bootstrap.setOption("tcpNoDelay", true);
    bootstrap.setOption("keepAlive", true);

    // connect
    ChannelFuture connectFuture = bootstrap.connect(new InetSocketAddress(host, port));
    _channel = connectFuture.awaitUninterruptibly().getChannel();
    if (!connectFuture.isSuccess()) {
      throw new RuntimeException("Unable to establist connection", connectFuture.getCause());
    }

  }

  public boolean process(Event event) {
    ChannelFuture future = _channel.write(event);
    EventClientHandler channelHandler = (EventClientHandler) future.getChannel().getPipeline().get(
        EventClientPipelineFactory.HANDLER);
    return channelHandler.get().booleanValue();

  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    // TODO do we need to cache the last written future to threadlocal and make
    // sure that this one is really written?
    _channel.close().awaitUninterruptibly();
  }
}
