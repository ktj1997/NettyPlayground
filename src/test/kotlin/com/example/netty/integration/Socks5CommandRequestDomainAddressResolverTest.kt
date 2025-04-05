package com.example.netty.integration

import com.example.netty.socks5.domain.command.Socks5CommandRequestDomainAddressResolver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Socks5CommandRequestDomainAddressResolverTest {
    @Test
    fun `test domain address resolver`() {
        val domain = "example.com"
        val payload = byteArrayOf(0x00, 0x00, 0x00, 0x00, domain.length.toByte()) + domain.toByteArray() + byteArrayOf(0x1F, 0x90.toByte())
        val resolver = Socks5CommandRequestDomainAddressResolver()
        val (address, port) = resolver.resolve(payload)
        assertEquals(domain, address)
        assertEquals(8080, port)
    }

    @Test
    fun `test domain address resolver with boundary values`() {
        val domain = "a".repeat(255)
        val payload = byteArrayOf(0x00, 0x00, 0x00, 0x00, domain.length.toByte()) + domain.toByteArray() + byteArrayOf(0x1F, 0x90.toByte())
        val resolver = Socks5CommandRequestDomainAddressResolver()
        val (address, port) = resolver.resolve(payload)

        assertEquals(domain, address)
        assertEquals(8080, port)
    }
}
