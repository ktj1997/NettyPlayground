package com.example.netty.socks5.domain

data class Socks5CommandRequest(
    val version: SocksVersion,
    val command: Socks5CommandType,
    private val reserved: Byte, // 예약필드 (미사용)
    val addressType: Socks5AddressType,
    val dstAddress: String,
    val dstPort: Int,
)
