package com.example.netty.echo.server

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "netty.server.tcp")
data class TcpEchoServerProperties(
    val echo: List<TcpEchoServerProperty>,
)

data class TcpEchoServerProperty(
    val identifier: String,
    val port: Int,
)
