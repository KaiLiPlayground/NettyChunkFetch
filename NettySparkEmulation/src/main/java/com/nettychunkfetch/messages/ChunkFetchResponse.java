package com.nettychunkfetch.messages;

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
}