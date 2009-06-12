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

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

/**
 * Creates a newly configured {@link ChannelPipeline} for a client-side channel.
 * 
 * @author The Netty Project (netty-dev@lists.jboss.org)
 * @author Trustin Lee (tlee@redhat.com)
 * 
 * @version $Rev: 55 $, $Date: 2008-08-12 20:58:15 +0900 (Tue, 12 Aug 2008) $
 */
public class EventClientPipelineFactory implements ChannelPipelineFactory {

  public static final String HANDLER = "handler";

  public EventClientPipelineFactory() {
  }

  public ChannelPipeline getPipeline() throws Exception {
    ChannelPipeline pipeline = pipeline();

    // Add the number codec first,
    pipeline.addLast("decoder", new ResponseDecoder());
    pipeline.addLast("encoder", new EventEncoder());

    // and then business logic.
    pipeline.addLast(HANDLER, new EventClientHandler());

    return pipeline;
  }
}
