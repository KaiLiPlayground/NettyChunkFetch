package com.nettychunkfetch.server;

import com.nettychunkfetch.messages.ChunkFetchRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import com.nettychunkfetch.messages.ChunkFetchResponse;

public class ServerHandler extends SimpleChannelInboundHandler<ChunkFetchRequest> {
    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChunkFetchRequest msg) {
        System.out.println("Server received fetch request for chunk: " + msg.getChunkId());
        //logger.debug("Server received fetch request for chunk: {}", msg.getChunkId());

        // Simulate fetching data. In a real application, fetch data from storage.
        byte[] data = ("data for chunk " + msg.getChunkId()).getBytes();
        ChunkFetchResponse response = new ChunkFetchResponse(msg.getChunkId(), data);
        ctx.writeAndFlush(response);
        //ctx.writeAndFlush(new ChunkFetchResponse("test", "response".getBytes()));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Server: Channel is active");
        //logger.debug("Server: Channel is active");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        //logger.error("Server channel exception", cause);
        ctx.close();
    }
}