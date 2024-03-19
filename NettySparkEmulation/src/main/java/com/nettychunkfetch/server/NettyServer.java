package com.nettychunkfetch.server;

import com.nettychunkfetch.codec.CatchAllHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import com.nettychunkfetch.codec.MessageDecoder;
import com.nettychunkfetch.codec.MessageEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyServer {
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("logger", new LoggingHandler(LogLevel.INFO));
                            ch.pipeline().addLast(new MessageDecoder(), new MessageEncoder(), new ServerHandler());
                            ch.pipeline().addLast("catch all handler", new CatchAllHandler());
                        }
                    });
           ChannelFuture f = b.bind(port).sync();
           f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
