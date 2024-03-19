package com.nettychunkfetch.server;

import com.nettychunkfetch.server.NettyServer;

public class ServerTest {
    public static void main(String[] args) throws Exception {
        NettyServer server = new NettyServer(8081);
        server.start(); // Start the server on port 8080
    }
}
