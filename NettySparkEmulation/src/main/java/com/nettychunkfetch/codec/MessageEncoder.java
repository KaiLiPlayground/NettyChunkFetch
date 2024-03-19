package com.nettychunkfetch.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import com.nettychunkfetch.messages.ChunkFetchRequest;
import com.nettychunkfetch.messages.ChunkFetchResponse;

public class MessageEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof ChunkFetchRequest) {
            out.writeByte(MessageType.REQUEST.getValue()); // Prefix with message type
            ByteBuf data = ((ChunkFetchRequest) msg).toByteBuf();
            out.writeInt(data.readableBytes());
            out.writeBytes(data);
        } else if (msg instanceof ChunkFetchResponse) {
            out.writeByte(MessageType.RESPONSE.getValue()); // Prefix with message type
            ByteBuf data = ((ChunkFetchResponse) msg).toByteBuf();
            out.writeInt(data.readableBytes());
            out.writeBytes(data);
        }
    }

}
