package com.example.netty.socks5.domain

interface Socks5Address {
    data class IPV4(
        val ip: String,
        val port: Int,
    ) : Socks5Address

    data object IPV6 : Socks5Address

    data object Domain : Socks5Address
}
