package com.example.netty.socks5.domain.command

interface Socks5CommandRequestAddressResolver {
    fun resolve(payload: ByteArray): Pair<String, Int>
}
