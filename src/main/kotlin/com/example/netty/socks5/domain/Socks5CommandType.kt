package com.example.netty.socks5.domain

import com.example.netty.socks5.domain.SocksVersion.UNKNOWN

enum class Socks5CommandType(
    val code: Int,
) {
    CONNECT(0x01),
    BIND(0x02),
    UDP_ASSOCIATE(0x03),
    UNKNOWN(-1),
    ;

    companion object {
        fun fromByte(byte: Byte): Socks5CommandType {
            return entries.firstOrNull { it.code == byte.toUByte().toInt() } ?: UNKNOWN
        }
    }
}
