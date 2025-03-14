package com.example.netty.socks5

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "netty.server.tcp.socks5")
class Socks5Properties(
    val properties: List<Socks5ServerProperty>,
)

data class Socks5ServerProperty(
    val identifier: String,
    val port: Int,
)
