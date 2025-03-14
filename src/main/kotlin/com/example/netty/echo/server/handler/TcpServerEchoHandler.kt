package com.example.netty.echo.server.handler

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class TcpServerEchoHandler : ChannelInboundHandlerAdapter() {
    override fun channelRead(
        ctx: ChannelHandlerContext,
        msg: Any,
    ) {
        if (msg is ByteBuf) {
            ctx.writeAndFlush(msg)
        }
    }
}
