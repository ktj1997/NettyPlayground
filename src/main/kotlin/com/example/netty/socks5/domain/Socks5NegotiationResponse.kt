package com.example.netty.socks5.domain

data class Socks5NegotiationResponse(
    val version: SocksVersion,
    val method: Socks5Method,
) : Socks5Message {
    override fun toBytes(): ByteArray {
        return byteArrayOf(
            version.code.toUByte().toByte(),
            method.code.toUByte().toByte(),
        )
    }

    companion object {
        fun fromByte(bytes: ByteArray): Socks5NegotiationResponse {
            return Socks5NegotiationResponse(
                version = SocksVersion.fromByte(bytes[0]),
                method = Socks5Method.fromByte(bytes[1]),
            )
        }
    }
}
