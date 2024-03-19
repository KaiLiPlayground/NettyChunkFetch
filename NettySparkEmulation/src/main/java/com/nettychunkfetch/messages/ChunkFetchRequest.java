package com.nettychunkfetch.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ChunkFetchRequest {
    private final String chunkId;

    public ChunkFetchRequest(String chunkId) {
        this.chunkId = chunkId;
    }

    public String getChunkId() {
        return chunkId;
    }

    // Serialize
    public ByteBuf toByteBuf() {
        return Unpooled.wrappedBuffer(this.chunkId.getBytes());
    }

    // Deserialize
    public static ChunkFetchRequest fromByteBuf(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return new ChunkFetchRequest(new String(bytes));
    }
}