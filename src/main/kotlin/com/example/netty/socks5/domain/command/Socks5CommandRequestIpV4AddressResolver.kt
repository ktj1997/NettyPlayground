package com.example.netty.socks5.domain.command

class Socks5CommandRequestIpV4AddressResolver : Socks5CommandRequestAddressResolver {
    override fun resolve(payload: ByteArray): Pair<String, Int> {
        val address = (4..7).joinToString(".") { payload[it].toUByte().toString() }
        val port = ((payload[8].toInt() and 0xFF) shl 8) or (payload[9].toInt() and 0xFF)
        return address to port
    }
}
