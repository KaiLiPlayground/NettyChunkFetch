package com.nettychunkfetch.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ChunkFetchResponse {
    private final String chunkId;
    private final byte[] data;

    public ChunkFetchResponse(String chunkId, byte[] data) {
        this.chunkId = chunkId;
        this.data = data;
    }

    public String getChunkId() {
        return chunkId;
    }

    public byte[] getData() {
        return data;
    }

    // Serialize
    public ByteBuf toByteBuf() {
        ByteBuf encoded = Unpooled.buffer();
        byte[] chunkIdBytes = this.chunkId.getBytes();
        encoded.writeInt(chunkIdBytes.length);
        encoded.writeBytes(chunkIdBytes);
        encoded.writeInt(data.length);
        encoded.writeBytes(data);
        return encoded;
    }

    // Deserialize
    public static ChunkFetchResponse fromByteBuf(ByteBuf byteBuf) {
        int chunkIdLength = byteBuf.readInt();
        byte[] chunkIdBytes = new byte[chunkIdLength];
        byteBuf.readBytes(chunkIdBytes);
        String chunkId = new String(chunkIdBytes);

        int dataLength = byteBuf.readInt();
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);

        return new ChunkFetchResponse(chunkId, data);
    }
}