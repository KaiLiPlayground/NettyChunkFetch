package com.nettychunkfetch.codec;

import com.nettychunkfetch.messages.ChunkFetchRequest;
import com.nettychunkfetch.messages.ChunkFetchResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 5) { // Byte for type + int for length
            return;
        }

        in.markReaderIndex();
        byte messageTypeByte = in.readByte();
        MessageType messageType = MessageType.fromInt(messageTypeByte);
        int length = in.readInt();

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        ByteBuf data = in.readSlice(length); // Use readSlice to avoid modifying reader index further
        switch (messageType) {
            case REQUEST:
                // Assuming you have a similar fromByteBuf method for ChunkFetchRequest
                out.add(ChunkFetchRequest.fromByteBuf(data));
                break;
            case RESPONSE:
                out.add(ChunkFetchResponse.fromByteBuf(data));
                break;
            default:
                throw new IllegalStateException("Unknown message type: " + messageType);
        }
    }
}
