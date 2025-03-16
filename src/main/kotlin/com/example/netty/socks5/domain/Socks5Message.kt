package com.example.netty.socks5.domain

interface Socks5Message {
    fun toBytes(): ByteArray
}
