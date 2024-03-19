package com.nettychunkfetch.client;

import com.nettychunkfetch.codec.MessageEncoder;
import com.nettychunkfetch.messages.ChunkFetchRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import com.nettychunkfetch.codec.MessageDecoder;

public class NettyClient {
    private final String host;
    private final int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void sendFetchRequest(String chunkId) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new MessageDecoder(), new MessageEncoder(), new ClientHandler());
                        }
                    });

            Channel channel = b.connect(host, port).sync().channel();
            ChunkFetchRequest request = new ChunkFetchRequest(chunkId);
            channel.writeAndFlush(request);
            channel.closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }
}
