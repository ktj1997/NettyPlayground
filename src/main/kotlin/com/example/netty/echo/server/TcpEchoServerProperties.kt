package com.example.netty.echo.server

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "netty.server.tcp.echo")
data class TcpEchoServerProperties(
    val properties: List<TcpEchoServerProperty>,
)

data class TcpEchoServerProperty(
    val identifier: String,
    val port: Int,
)
