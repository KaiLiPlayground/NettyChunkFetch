package com.nettychunkfetch;

import com.nettychunkfetch.client.NettyClient;
import com.nettychunkfetch.server.NettyServer;

import java.util.Random;

public class ApplicationTest {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        new Thread(() -> {
            try {
                new NettyServer(port).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(1000); // Wait for the server to start

        NettyClient client = new NettyClient("localhost", port);
        Random ran = new Random(500);

        while (true)
        {
            client.sendFetchRequest("chunk" + ran.nextInt(10));
        }

    }
}
