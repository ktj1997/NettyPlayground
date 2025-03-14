package com.example.netty.socks5

import com.example.netty.NettyServerManager
import com.example.netty.common.ExceptionHandler
import com.example.netty.common.TcpInboundLoggingHandler
import com.example.netty.socks5.domain.SocksMethodSelector
import com.example.netty.socks5.handler.SocksNegotiationHandler
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
            add(TcpInboundLoggingHandler(identifier))
            add(SocksNegotiationHandler(SocksMethodSelector()))
            add(ExceptionHandler(identifier))
        }
    }
}
