/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @author tags. See the COPYRIGHT.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package net.sf.cesto.nio;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

@ChannelPipelineCoverage("one")
public class EventClientHandler extends SimpleChannelHandler {

  private static final Logger logger = Logger.getLogger(EventClientHandler.class.getName());

  final BlockingQueue<Boolean> answer = new LinkedBlockingQueue<Boolean>();

  public EventClientHandler() {
  }

  public Boolean get() {
    for (;;) {
      try {
        return answer.take();
      } catch (InterruptedException e) {
        // Ignore.
      }
    }
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) {
    // put response into queue
    // e.getChannel().i
    // e.getChannel().close().addListener(new ChannelFutureListener() {
    // public void operationComplete(ChannelFuture future) {
    answer.offer((Boolean) e.getMessage());
    // }
    // });
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
    logger.log(Level.WARNING, "Unexpected exception from downstream.", e.getCause());
    e.getChannel().close();
  }

}
