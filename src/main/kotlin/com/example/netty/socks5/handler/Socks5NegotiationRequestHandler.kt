package com.example.netty.socks5.handler

import com.example.netty.socks5.domain.Socks5MethodSelector
import com.example.netty.socks5.domain.Socks5NegotiationRequest
import com.example.netty.socks5.domain.Socks5NegotiationResponse
import com.example.netty.socks5.domain.Socks5Status
import com.example.netty.socks5.domain.SocksVersion
import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

@Sharable
class Socks5NegotiationRequestHandler(
    private val socks5MethodSelector: Socks5MethodSelector,
) : ChannelInboundHandlerAdapter() {
    override fun channelRead(
        ctx: ChannelHandlerContext,
        msg: Any,
    ) {
        val status = ctx.channel().attr(Socks5Status.KEY)
        if (msg is Socks5NegotiationRequest && status.get() == Socks5Status.NEGOTIATION) {
            if (msg.version != SocksVersion.SOCKS5) {
                throw IllegalArgumentException("Unsupported Version: ${msg.version.name}")
            }

            val selectedMethod = socks5MethodSelector.select()
            logger.info { "[Socks5][NegotiationRequestHandle] (method: $selectedMethod) " }

            val response =
                Socks5NegotiationResponse(
                    version = SocksVersion.SOCKS5,
                    method = selectedMethod,
                )
            ctx.write(response).addListener {
                if (it.isSuccess) {
                    logger.error { "[Socks5NegotiationRequestHandler][NegotiationRequestHandle][Success]" }
                    status.set(Socks5Status.COMMAND)
                } else {
                    logger.error { "[Socks5NegotiationRequestHandler][NegotiationRequestHandle][Fail] (cause :${it.cause().message})" }
                }
            }
        } else {
            ctx.fireChannelRead(msg)
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
