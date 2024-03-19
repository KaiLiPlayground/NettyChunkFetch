package com.nettychunkfetch.client;

import com.nettychunkfetch.client.NettyClient;

import java.util.Random;

public class ClientTest {
    public static void main(String[] args) throws Exception {
        NettyClient client = new NettyClient("localhost", 8081);
        Random ran = new Random();
        while (true)
        {
            client.sendFetchRequest("chunk" + ran.nextInt(100)); // Send a request for "chunk1"
        }
        //client.sendFetchRequest("chunk10");
    }
}
