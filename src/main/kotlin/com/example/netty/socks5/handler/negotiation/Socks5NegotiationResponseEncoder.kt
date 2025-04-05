package com.example.netty.socks5.handler.negotiation

import com.example.netty.socks5.domain.Socks5NegotiationResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class Socks5NegotiationResponseEncoder : MessageToByteEncoder<Socks5NegotiationResponse>() {
    override fun encode(
        ctx: ChannelHandlerContext,
        msg: Socks5NegotiationResponse,
        out: ByteBuf,
    ) {
        logger.info { "[Socks5NegotiationHandler][Response] $msg" }
        out.writeBytes(msg.toBytes())
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
