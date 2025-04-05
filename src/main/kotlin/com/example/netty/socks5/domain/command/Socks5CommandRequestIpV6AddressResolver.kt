package com.example.netty.socks5.domain.command

class Socks5CommandRequestIpV6AddressResolver : Socks5CommandRequestAddressResolver {
    override fun resolve(payload: ByteArray): Pair<String, Int> {
        val address =
            (4..19).chunked(2).joinToString(":") {
                payload[it[0]].toUByte().toString(16).padStart(2, '0') +
                    payload[it[1]].toUByte().toString(16).padStart(2, '0')
            }
        val port = ((payload[20].toInt() and 0xFF) shl 8) or (payload[21].toInt() and 0xFF)
        return address to port
    }
}
