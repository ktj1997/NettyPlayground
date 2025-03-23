package com.example.netty.socks5.domain

enum class Socks5AddressType(val code: Int) {
    IPV4(0x01),
    DOMAIN(0x03),
    IPV6(0x04),
    UNKNOWN(-1),
    ;

    companion object {
        fun fromByte(byte: Byte): Socks5AddressType {
            return entries.firstOrNull { it.code == byte.toUByte().toInt() } ?: UNKNOWN
        }
    }
}
