package com.example.netty.integration

import com.example.netty.socks5.domain.command.Socks5CommandRequestIpV6AddressResolver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Socks5CommandRequestIpV6AddressResolverTest {
    @Test
    fun `test IPv6 address resolver`() {
        val payload =
            byteArrayOf(
                0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
                0x20.toByte(), 0x01.toByte(), 0x0d.toByte(), 0xb8.toByte(),
                0x85.toByte(), 0xa3.toByte(), 0x00.toByte(), 0x00.toByte(),
                0x00.toByte(), 0x00.toByte(), 0x8a.toByte(), 0x2e.toByte(),
                0x03.toByte(), 0x70.toByte(), 0x73.toByte(), 0x34.toByte(),
                0x1F.toByte(), 0x90.toByte(),
            )
        val resolver = Socks5CommandRequestIpV6AddressResolver()
        val (address, port) = resolver.resolve(payload)
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", address)
        assertEquals(8080, port)
    }

    @Test
    fun `test IPv6 address resolver with boundary values`() {
        val payload =
            byteArrayOf(
                0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
                0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
                0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
                0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
                0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
                0xFF.toByte(),0xFF.toByte(),
            )
        val resolver = Socks5CommandRequestIpV6AddressResolver()
        val (address, port) = resolver.resolve(payload)
        assertEquals("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff", address)
        assertEquals(65535, port)
    }
}
