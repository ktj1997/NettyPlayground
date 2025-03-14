package com.example.netty.socks5.handler

import com.example.netty.common.util.getNBytes
import com.example.netty.common.util.readNBytes
import com.example.netty.socks5.domain.SocksMethodSelector
import com.example.netty.socks5.domain.SocksNegotiationRequest
import com.example.netty.socks5.domain.SocksNegotiationResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

class SocksNegotiationHandler(
    private val methodSelector: SocksMethodSelector,
) : ByteToMessageDecoder() {
    override fun decode(
        ctx: ChannelHandlerContext,
        input: ByteBuf,
        output: MutableList<Any>,
    ) {
        if (input.readableBytes() < 2) return

        val minimumBytes = input.getNBytes(2)
        val methodCount = minimumBytes[1].toUByte().toInt()
        val negotiationMessageLength = 2 + methodCount

        if (input.readableBytes() < negotiationMessageLength) return

        val negotiationRequest = SocksNegotiationRequest.fromBytes(input.readNBytes(negotiationMessageLength))
        logger.info { "[Socks5NegotiationHandler][Request] $negotiationRequest" }

        val negotiationResponse =
            SocksNegotiationResponse(
                version = negotiationRequest.version,
                method = methodSelector.select(),
            )
        logger.info { "[Socks5NegotiationHandler][Response] $negotiationResponse" }
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(negotiationResponse.toBytes()))
            .addListener {
                ctx.pipeline().remove(this)
            }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
