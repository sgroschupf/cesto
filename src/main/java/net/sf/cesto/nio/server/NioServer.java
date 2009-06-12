package net.sf.cesto.nio.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import net.sf.cesto.processors.IProcessor;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class NioServer {

  public NioServer(int port, IProcessor processor) {
    ChannelFactory factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors
        .newCachedThreadPool());

    ServerBootstrap bootstrap = new ServerBootstrap(factory);
    // TODO put in a real processor
    bootstrap.setPipelineFactory(new EventServerPipelineFactory(processor));
    bootstrap.setOption("child.tcpNoDelay", true);
    bootstrap.setOption("child.keepAlive", true);

    // Bind and start to accept incoming connections.

    // TODO define bining address.

    bootstrap.bind(new InetSocketAddress(port));
  }

  public static void main(String[] args) throws Exception {
    // Configure the server.
  }
}
