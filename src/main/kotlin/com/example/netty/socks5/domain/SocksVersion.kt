package com.example.netty.socks5.domain

enum class SocksVersion(
    val code: Int,
) {
    SOCKS5(0x05),
    UNKNOWN(-1),
    ;

    companion object {
        fun fromByte(byte: Byte): SocksVersion {
            return SocksVersion.entries.firstOrNull { it.code == byte.toUByte().toInt() } ?: UNKNOWN
        }
    }
}
