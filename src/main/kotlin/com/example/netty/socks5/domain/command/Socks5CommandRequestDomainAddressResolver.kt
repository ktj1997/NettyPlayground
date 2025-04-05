package com.example.netty.socks5.domain.command

class Socks5CommandRequestDomainAddressResolver : Socks5CommandRequestAddressResolver {
    override fun resolve(payload: ByteArray): Pair<String, Int> {
        val domainLength = payload[4].toUByte().toInt()
        val domain = String(payload, 5, domainLength)
        val port = ((payload[5 + domainLength].toInt() and 0xFF) shl 8) or (payload[6 + domainLength].toInt() and 0xFF)
        return domain to port
    }
}
