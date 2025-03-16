package com.example.netty.socks5.handler

import com.example.netty.common.util.getNBytes
import com.example.netty.common.util.readNBytes
import com.example.netty.socks5.domain.Socks5NegotiationRequest
import com.example.netty.socks5.domain.Socks5Status
import com.example.netty.socks5.domain.SocksVersion
import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.DecoderException

class Socks5NegotiationRequestDecoder : ByteToMessageDecoder() {
    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.channel().attr(Socks5Status.KEY).set(Socks5Status.NEGOTIATION)
        ctx.fireChannelActive()
    }

    override fun decode(
        ctx: ChannelHandlerContext,
        input: ByteBuf,
        output: MutableList<Any>,
    ) {
        val status = ctx.channel().attr(Socks5Status.KEY).get()

        if (status == Socks5Status.NEGOTIATION) {
            if (input.readableBytes() < 2) return

            val minimumBytes = input.getNBytes(2)
            val socksVersion = SocksVersion.fromByte(minimumBytes[0])

            if (socksVersion != SocksVersion.SOCKS5) {
                throw DecoderException("UnsupportedVersion SocksVersion")
            }

            val methodCount = minimumBytes[1].toUByte().toInt()
            val negotiationMessageLength = 2 + methodCount

            if (input.readableBytes() < negotiationMessageLength) return

            val negotiationRequest = Socks5NegotiationRequest.fromBytes(input.readNBytes(negotiationMessageLength))
            logger.info { "[Socks5NegotiationHandler][Request] $negotiationRequest" }

            output.add(negotiationRequest)
        } else {
            // bypass
            output.add(Unpooled.copiedBuffer(input))
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
