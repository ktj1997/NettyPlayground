package com.example.netty.socks5.domain

enum class Socks5Method(val code: Int) {
    NO_AUTH(0x00),
    GSSAPI(0x01),
    USERNAME_PASSWORD(0x02),
    NO_ACCEPTABLE(0xFF),
    UNKNOWN(-1),
    ;

    companion object {
        fun fromByte(byte: Byte): Socks5Method {
            return Socks5Method.entries.firstOrNull { it.code == byte.toUByte().toInt() } ?: UNKNOWN
        }
    }
}
