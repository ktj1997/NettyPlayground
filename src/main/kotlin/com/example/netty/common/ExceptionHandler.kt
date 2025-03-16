package com.example.netty.common

import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

@Sharable
class ExceptionHandler(
    private val identifier: String,
) : ChannelInboundHandlerAdapter() {
    override fun exceptionCaught(
        ctx: ChannelHandlerContext,
        throwable: Throwable,
    ) {
        ctx.channel().close().addListener { future ->
            logger.error { "channel close due to exception (identifier: $identifier, cause: ${throwable.message})" }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
