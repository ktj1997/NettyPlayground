package com.example.netty.echo.server

import com.example.netty.NettyServerManager
import com.example.netty.common.ExceptionHandler
import com.example.netty.common.TcpInboundOutboundLoggingHandler
import com.example.netty.echo.server.handler.TcpServerEchoHandler
import io.netty.channel.ChannelHandler
import org.springframework.stereotype.Component

@Component
class TcpEchoServerManager(
    private val properties: TcpEchoServerProperties,
) : NettyServerManager {
    private val servers = mutableListOf<TcpEchoServer>()

    override fun startAll() {
        properties.properties.forEach { property ->
            servers.add(
                TcpEchoServer(
                    port = property.port,
                    handlers = { handlers(property.identifier) },
                ).also { it.start() },
            )
        }
    }

    override fun stopAll() {
        servers.forEach {
            it.stop()
        }
    }

    private fun handlers(identifier: String): List<ChannelHandler> {
        return buildList {
            add(TcpInboundOutboundLoggingHandler(identifier))
            add(TcpServerEchoHandler())
            add(ExceptionHandler(identifier))
        }
    }
}
