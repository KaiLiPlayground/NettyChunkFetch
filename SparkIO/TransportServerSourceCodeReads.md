The `TransportServer.java` class from the Apache Spark repository defines a server component for Spark's low-level streaming service. This class sets up a network server using Netty, a high-performance asynchronous event-driven network application framework. Each method in this class plays a specific role in setting up and managing the server. Let's go through the main methods, with a particular focus on the `init()` method.

### Constructor: `TransportServer(...)`

The constructor initializes a new instance of `TransportServer` with specified parameters such as the host and port to bind to, an `RpcHandler` to handle RPC calls, and a list of `TransportServerBootstrap` to perform additional bootstrapping. It sets up byte buffer allocators (either shared or exclusive) based on the configuration, stores the provided parameters, and then calls `init()` to start the server. If initialization fails, it closes the server to prevent resource leaks.

### `getPort()`

This method returns the port number the server is bound to. If the server hasn't been initialized yet (port is `-1`), it throws an `IllegalStateException`.

### `init(String hostToBind, int portToBind)`

The `init()` method is where the actual server setup happens. Let's break it down:

1. **Determines I/O mode:** Based on the configuration (`conf.ioMode()`), it decides whether to use NIO (Non-blocking I/O) or EPOLL (a more efficient I/O operation mechanism available on Linux) for network communication.

2. **EventLoopGroup creation:** Creates two `EventLoopGroup` instances for handling network events. The "boss" group accepts incoming connections, and the "worker" group handles the data traffic of the accepted connections. The number of threads for the worker group is determined by the server's configuration.

3. **ServerBootstrap configuration:** Configures the `ServerBootstrap`, a helper class in Netty for setting up the server. It specifies the channel type based on the I/O mode, sets various options like buffer allocators, SO_REUSEADDR, TCP keep-alive, send and receive buffer sizes, and backlog size. Child options are also set, which apply to the channels accepted by the server.

4. **Channel initialization:** Attaches a `ChannelInitializer` to the bootstrap. This initializer is called once a connection is accepted. It sets up the pipeline for the new channel, including applying any additional bootstraps and initializing it with the `RpcHandler`.

5. **Binding to address:** Binds the server to the specified host and port. If `hostToBind` is `null`, it binds to any available host.

6. **Post-bind operations:** After binding, it retrieves the actual port the server is listening on and logs the server's start information.

This method sets up the server to accept connections and handle them according to the provided `RpcHandler` and any additional bootstraps.

### `getAllMetrics()`

Returns a set of metrics related to memory usage by the server, useful for monitoring and debugging.

### `close()`

Cleans up resources by closing the server channel and shutting down the event loop groups gracefully.

### `getRegisteredConnections()`

Returns a counter of currently registered connections, allowing for monitoring the number of active connections to the server.

The `init()` method is crucial as it encapsulates the core logic for server setup, including configuring network options, setting up event loops, and binding to a port to start accepting connections.
