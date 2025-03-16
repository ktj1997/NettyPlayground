package com.example.netty.common

import com.example.netty.common.util.toPrettyFormat
import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise

class TcpInboundOutboundLoggingHandler(
    private val identifier: String,
) : ChannelDuplexHandler() {
    override fun channelRead(
        ctx: ChannelHandlerContext,
        msg: Any,
    ) {
        if (msg is ByteBuf) {
            logger.info {
                "[TcpInboundOutboundLoggingHandler][$identifier] (read ${msg.readableBytes()}bytes) \n  ${ByteBufUtil.getBytes(
                    msg,
                ).toPrettyFormat()}"
            }
        }

        ctx.fireChannelRead(msg)
    }

    override fun write(
        ctx: ChannelHandlerContext,
        msg: Any,
        promise: ChannelPromise,
    ) {
        if (msg is ByteBuf) {
            logger.info {
                "[TcpInboundOutboundLoggingHandler][$identifier] (write ${msg.readableBytes()}bytes) \n  ${ByteBufUtil.getBytes(
                    msg,
                ).toPrettyFormat()}"
            }
        }
        ctx.writeAndFlush(msg, promise)
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
