package com.example.netty.socks5.domain

data class SocksNegotiationRequest(
    val version: SocksVersion,
    val methodCount: Int,
    val candidates: List<SocksMethod>,
) {
    fun toBytes(): ByteArray {
        return byteArrayOf(
            version.code.toUByte().toByte(),
            methodCount.toUByte().toByte(),
        ) + candidates.map { it.code.toUByte().toByte() }.toByteArray()
    }

    companion object {
        fun fromBytes(bytes: ByteArray): SocksNegotiationRequest {
            return SocksNegotiationRequest(
                version = SocksVersion.fromByte(bytes[0]),
                methodCount = bytes[1].toUByte().toInt(),
                candidates = (2..<bytes.size).map { SocksMethod.fromByte(bytes[it]) },
            )
        }
    }
}
