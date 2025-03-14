package com.example.netty.socks5.domain

data class SocksNegotiationResponse(
    val version: SocksVersion,
    val method: SocksMethod,
) {
    fun toBytes(): ByteArray {
        return byteArrayOf(
            version.code.toUByte().toByte(),
            method.code.toUByte().toByte(),
        )
    }

    companion object {
        fun fromByte(bytes: ByteArray): SocksNegotiationResponse {
            return SocksNegotiationResponse(
                version = SocksVersion.fromByte(bytes[0]),
                method = SocksMethod.fromByte(bytes[1]),
            )
        }
    }
}
