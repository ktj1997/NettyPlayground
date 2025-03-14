package com.example.netty.common

import com.example.netty.common.util.toPrettyFormat
import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class TcpInboundLoggingHandler(
    private val identifier: String,
) : ChannelInboundHandlerAdapter() {
    override fun channelRead(
        ctx: ChannelHandlerContext,
        msg: Any,
    ) {
        if (msg is ByteBuf) {
            logger.info {
                "[TcpInboundLogger][$identifier] (read ${msg.readableBytes()}bytes) \n  ${ByteBufUtil.getBytes(
                    msg,
                ).toPrettyFormat()}"
            }
        }
        super.channelRead(ctx, msg)
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
