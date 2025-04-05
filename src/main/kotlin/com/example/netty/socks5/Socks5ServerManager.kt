package com.example.netty.socks5

import Socks5CommandRequestDecoder
import com.example.netty.NettyServerManager
import com.example.netty.common.ExceptionHandler
import com.example.netty.common.TcpInboundOutboundLoggingHandler
import com.example.netty.socks5.domain.Socks5MethodSelector
import com.example.netty.socks5.domain.command.Socks5CommandRequestAddressResolverFactory
import com.example.netty.socks5.handler.negotiation.Socks5NegotiationRequestDecoder
import com.example.netty.socks5.handler.negotiation.Socks5NegotiationRequestHandler
import com.example.netty.socks5.handler.negotiation.Socks5NegotiationResponseEncoder
import io.netty.channel.ChannelHandler
import org.springframework.stereotype.Component

@Component
class Socks5ServerManager(
    private val properties: Socks5Properties,
) : NettyServerManager {
    private val servers = mutableListOf<Socks5Server>()

    override fun startAll() {
        properties.properties.forEach { property ->
            servers.add(
                Socks5Server(
                    port = property.port,
                    handlers = { handlers(property.identifier) },
                ).also { it.start() },
            )
        }
    }

    override fun stopAll() {
        servers.forEach { it.stop() }
    }

    private fun handlers(identifier: String): List<ChannelHandler> {
        return buildList {
            add(TcpInboundOutboundLoggingHandler(identifier))
            add(Socks5NegotiationResponseEncoder())
            add(Socks5NegotiationRequestDecoder())
            add(Socks5NegotiationRequestHandler(Socks5MethodSelector()))
            add(Socks5CommandRequestDecoder(Socks5CommandRequestAddressResolverFactory()))
            add(ExceptionHandler(identifier))
        }
    }
}
