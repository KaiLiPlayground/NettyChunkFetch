package com.nettychunkfetch.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import com.nettychunkfetch.messages.ChunkFetchResponse;

public class ClientHandler extends SimpleChannelInboundHandler<ChunkFetchResponse> {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChunkFetchResponse msg) {
        System.out.println("Client received data for chunk: " + msg.getChunkId());
        System.out.println("Data: " + new String(msg.getData()));
        //logger.debug("Client received data for chunk: {}", msg.getChunkId());
        //logger.debug("Data: ", new String(msg.getData()));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Client: Channel is active");
        // logger.debug("Client: Channel is active");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        // logger.error("Client channel exception", cause);
        ctx.close();
    }
}
