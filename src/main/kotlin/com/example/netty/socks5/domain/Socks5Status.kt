package com.example.netty.socks5.domain

import io.netty.util.AttributeKey

enum class Socks5Status {
    NEGOTIATION,
    COMMAND,
    PROXY,
    ;

    companion object {
        val KEY: AttributeKey<Socks5Status> = AttributeKey.valueOf(Socks5Status::class.java.simpleName)
    }
}
