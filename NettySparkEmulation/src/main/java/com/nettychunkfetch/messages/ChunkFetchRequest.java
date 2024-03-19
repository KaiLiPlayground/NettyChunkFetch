package com.nettychunkfetch.messages;

public class ChunkFetchRequest {
    private final String chunkId;

    public ChunkFetchRequest(String chunkId) {
        this.chunkId = chunkId;
    }

    public String getChunkId() {
        return chunkId;
    }
}