# Log

```log
based on your suggested changes on the message encoder and decoder,

ServerTest output:
========
22:30:01.628 [nioEventLoopGroup-3-1] INFO  i.n.handler.logging.LoggingHandler - [id: 0x946498a9, L:/127.0.0.1:8081 - R:/127.0.0.1:61508] REGISTERED
22:30:01.628 [nioEventLoopGroup-3-1] INFO  i.n.handler.logging.LoggingHandler - [id: 0x946498a9, L:/127.0.0.1:8081 - R:/127.0.0.1:61508] ACTIVE
Server: Channel is active
22:30:01.632 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.maxCapacityPerThread: 4096
22:30:01.632 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.ratio: 8
22:30:01.632 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.chunkSize: 32
22:30:01.632 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.blocking: false
22:30:01.632 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.batchFastThreadLocalOnly: true
22:30:01.641 [nioEventLoopGroup-3-1] DEBUG io.netty.buffer.AbstractByteBuf - -Dio.netty.buffer.checkAccessible: true
22:30:01.641 [nioEventLoopGroup-3-1] DEBUG io.netty.buffer.AbstractByteBuf - -Dio.netty.buffer.checkBounds: true
22:30:01.642 [nioEventLoopGroup-3-1] DEBUG i.n.util.ResourceLeakDetectorFactory - Loaded default ResourceLeakDetector: io.netty.util.ResourceLeakDetector@412f8d95
22:30:01.648 [nioEventLoopGroup-3-1] INFO  i.n.handler.logging.LoggingHandler - [id: 0x946498a9, L:/127.0.0.1:8081 - R:/127.0.0.1:61508] READ: 11B
         +-------------------------------------------------+
         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+----------------+
|00000000| 00 00 00 07 63 68 75 6e 6b 31 30                |....chunk10     |
+--------+-------------------------------------------------+----------------+
CatchAllHandler received message: PooledUnsafeDirectByteBuf(ridx: 0, widx: 7, cap: 7)
22:30:01.657 [nioEventLoopGroup-3-1] INFO  i.n.handler.logging.LoggingHandler - [id: 0x946498a9, L:/127.0.0.1:8081 - R:/127.0.0.1:61508] READ COMPLETE
========
ClientTest output: 
========
22:30:01.573 [nioEventLoopGroup-2-1] INFO  i.n.handler.logging.LoggingHandler - [id: 0x7070d036] REGISTERED
22:30:01.574 [nioEventLoopGroup-2-1] INFO  i.n.handler.logging.LoggingHandler - [id: 0x7070d036] CONNECT: localhost/127.0.0.1:8081
22:30:01.577 [nioEventLoopGroup-2-1] INFO  i.n.handler.logging.LoggingHandler - [id: 0x7070d036, L:/127.0.0.1:61508 - R:localhost/127.0.0.1:8081] ACTIVE
Client: Channel is active
22:30:01.582 [main] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.maxCapacityPerThread: 4096
22:30:01.582 [main] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.ratio: 8
22:30:01.582 [main] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.chunkSize: 32
22:30:01.582 [main] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.blocking: false
22:30:01.582 [main] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.batchFastThreadLocalOnly: true
22:30:01.593 [nioEventLoopGroup-2-1] DEBUG io.netty.buffer.AbstractByteBuf - -Dio.netty.buffer.checkAccessible: true
22:30:01.593 [nioEventLoopGroup-2-1] DEBUG io.netty.buffer.AbstractByteBuf - -Dio.netty.buffer.checkBounds: true
22:30:01.594 [nioEventLoopGroup-2-1] DEBUG i.n.util.ResourceLeakDetectorFactory - Loaded default ResourceLeakDetector: io.netty.util.ResourceLeakDetector@75c32c84
22:30:01.601 [nioEventLoopGroup-2-1] INFO  i.n.handler.logging.LoggingHandler - [id: 0x7070d036, L:/127.0.0.1:61508 - R:localhost/127.0.0.1:8081] WRITE: 11B
         +-------------------------------------------------+
         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+----------------+
|00000000| 00 00 00 07 63 68 75 6e 6b 31 30                |....chunk10     |
+--------+-------------------------------------------------+----------------+
22:30:01.602 [nioEventLoopGroup-2-1] INFO  i.n.handler.logging.LoggingHandler - [id: 0x7070d036, L:/127.0.0.1:61508 - R:localhost/127.0.0.1:8081] FLUSH
========

NettyServer.java
=========
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

=========

NettyClient.java
=========
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
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

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
                            ch.pipeline().addLast("logger", new LoggingHandler(LogLevel.INFO));
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

=========

As per the above provided information, the "channelRead0" for both handlers are still not printing any output. 

but the CatchAllHandler is ablet to capture some issue from the server test output: "CatchAllHandler received message: PooledUnsafeDirectByteBuf(ridx: 0, widx: 7, cap: 7)"
```

Given the logging outputs you've shared, it's clear that there's an issue with the message being sent and the serialization/deserialization process. The `LoggingHandler` output indicates that data is being sent and received, but the fact that `channelRead0` is not being invoked suggests that the message isn't being correctly decoded into the expected object type, preventing it from reaching the `ServerHandler` and `ClientHandler` `channelRead0` methods.

One potential issue could be with the way the serialization and deserialization are implemented, particularly in how the message length and content are handled. Let's simplify the process to ensure that the message structure is correctly formed and recognized by the decoder.

### Simplifying Message Encoding and Decoding

The primary goal is to make sure that the encoded message includes enough information for the decoder to recognize and extract the complete message, including a message length prefix to indicate how many bytes need to be read for the complete message.

#### Adjusting MessageEncoder

The `MessageEncoder` should prepend the length of the message before the actual data, allowing the `MessageDecoder` to know exactly how much data it needs to wait for before it attempts to decode a message.

```java
package com.nettychunkfetch.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import com.nettychunkfetch.messages.ChunkFetchRequest;
import com.nettychunkfetch.messages.ChunkFetchResponse;

public class MessageEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof ChunkFetchRequest) {
            ByteBuf encoded = ((ChunkFetchRequest) msg).toByteBuf();
            out.writeInt(encoded.readableBytes()); // Length of the message
            out.writeBytes(encoded);
        } else if (msg instanceof ChunkFetchResponse) {
            ByteBuf encoded = ((ChunkFetchResponse) msg).toByteBuf();
            out.writeInt(encoded.readableBytes()); // Length of the message
            out.writeBytes(encoded);
        }
    }
}
```

#### Adjusting MessageDecoder

The `MessageDecoder` needs to correctly interpret this structure, waiting for the length information before attempting to decode the actual message content.

```java
package com.nettychunkfetch.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) { // Ensure there's enough data for an integer length
            return; // Not enough data - exit
        }

        in.markReaderIndex(); // Mark current position
        int length = in.readInt(); // Read length of the next message

        if (in.readableBytes() < length) {
            in.resetReaderIndex(); // Reset to marked position if not enough data for a complete message
            return; // Exit and wait for more data
        }

        // There's enough data for a complete message
        ByteBuf result = in.readBytes(length); // Extract the part of the buffer that represents a complete message
        out.add(result); // Pass the extracted data for further processing
    }
}
```

### Testing

After implementing these changes:

- Restart your server and client to test the updated implementation.
- Observe if the `channelRead0` methods in both `ServerHandler` and `ClientHandler` are now being invoked as expected.

### Debugging

If issues persist:

- Double-check that the serialization (`toByteBuf`) and deserialization (`fromByteBuf`) logic matches exactly in terms of the order and size of the data being read and written.
- Ensure that no additional unwritten bytes are being interpreted as part of the message content, which could throw off the decoding process.
- Consider adding more logging at various points in the encoding and decoding process to trace the flow of data and identify where it might be getting misinterpreted.

This approach provides a more explicit handling of message boundaries, which should help in ensuring that your messages are correctly framed and recognized by the Netty pipeline.
