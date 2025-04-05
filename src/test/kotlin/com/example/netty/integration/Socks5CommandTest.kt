package com.example.netty.integration

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.Socket

class Socks5CommandTest {
    @Test
    @Disabled
    fun `resolve_IPV4_address`() {
        val proxy = Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved("127.0.0.1", 1080))
        val socket = Socket(proxy).also { it.connect(InetSocketAddress("192.168.1.1", 8080)) }
    }

    @Test
    @Disabled
    fun `resolve_IPV6_address`() {
        val proxy = Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved("127.0.0.1", 1080))
        val socket = Socket(proxy).also { it.connect(InetSocketAddress("2001:db8:85a3::8a2e:370:7334", 8080)) }
    }

    @Test
    @Disabled
    fun `resolve_DOMAIN_address`() {
        val proxy = Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved("127.0.0.1", 1080))
        val socket = Socket(proxy).also { it.connect(InetSocketAddress.createUnresolved("example.com", 8080)) }
    }
}
