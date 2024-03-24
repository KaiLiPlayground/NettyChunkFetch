In Spark's Netty-based transport layer, the construction of server (`TransportServer`) and client (`TransportClient` via `TransportClientFactory`) applications involves several key Netty concepts such as bootstrap, event loop groups, channels, channel pipelines, handlers, and channel futures. Here's how they are utilized:

### Server Construction (`TransportServer`)

1. **Bootstrap**: A `ServerBootstrap` instance is created, configuring the server.
2. **EventLoopGroup**: Two event loop groups are defined - one for accepting incoming connections (`bossGroup`) and another for handling the traffic of the accepted connections (`workerGroup`). The `NettyUtils.createEventLoop()` utility method is used based on the I/O mode (NIO or EPOLL).
3. **Channel**: The server channel class is determined based on the I/O mode, and specified in the bootstrap configuration (`bootstrap.channel(NettyUtils.getServerChannelClass(ioMode))`).
4. **Channel Option**: Various TCP channel options such as `TCP_NODELAY`, `SO_KEEPALIVE`, etc., are set to optimize performance.
5. **Channel Pipeline**: A `ChannelInitializer` is attached to the bootstrap. It initializes each new `SocketChannel` with a pipeline that includes the `TransportChannelHandler`, which in turn contains a `TransportClient` and response/request handlers. The `TransportContext.initializePipeline()` method is called to set up encoding/decoding and to manage the channel's I/O events.
6. **Handlers**: Custom handlers, including those for managing RPC messages and chunked data streams, are added to the pipeline.
7. **ChannelFuture**: The `bind()` method on the bootstrap instance is called to start the server and bind to a specific port. The returned `ChannelFuture` is used to asynchronously handle the operation's outcome.

### Client Construction (`TransportClientFactory`)

1. **Bootstrap**: A `Bootstrap` instance is created for configuring client connections.
2. **EventLoopGroup**: A single event loop group is created for handling all client-side traffic, using `NettyUtils.createEventLoop()`.
3. **Channel**: The client channel class is determined (e.g., `NioSocketChannel` for NIO mode) and set in the bootstrap configuration.
4. **Channel Option**: Channel options similar to the server are set to optimize connection performance.
5. **Channel Pipeline**: As with the server, a `ChannelInitializer` is used to initialize each new `SocketChannel`'s pipeline. It configures the pipeline with encoding/decoding handlers and the `TransportChannelHandler`. The client's pipeline setup also calls `TransportContext.initializePipeline()`, but with client-specific handlers.
6. **Handlers**: Additional bootstraps (`TransportClientBootstrap`) may be run against the `TransportClient` for custom initialization before the connection is considered established.
7. **ChannelFuture**: The `connect()` method on the bootstrap is called to initiate the connection to the server. The returned `ChannelFuture` is used to wait for the connection attempt to succeed or fail. Once connected, the `TransportClient` is returned for sending data.

Both server and client heavily rely on the asynchronous and event-driven nature of Netty, utilizing futures to handle operations that might not complete immediately and event loops to process I/O events efficiently. This design allows Spark to manage high-throughput, low-latency network communications between nodes in a distributed environment.
