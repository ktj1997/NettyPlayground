package com.example.netty.socks5.domain

data class Socks5NegotiationRequest(
    val version: SocksVersion,
    val methodCount: Int,
    val candidates: List<Socks5Method>,
) : Socks5Message {
    override fun toBytes(): ByteArray {
        return byteArrayOf(
            version.code.toUByte().toByte(),
            methodCount.toUByte().toByte(),
        ) + candidates.map { it.code.toUByte().toByte() }.toByteArray()
    }

    companion object {
        fun fromBytes(bytes: ByteArray): Socks5NegotiationRequest {
            return Socks5NegotiationRequest(
                version = SocksVersion.fromByte(bytes[0]),
                methodCount = bytes[1].toUByte().toInt(),
                candidates = (2..<bytes.size).map { Socks5Method.fromByte(bytes[it]) },
            )
        }
    }
}
