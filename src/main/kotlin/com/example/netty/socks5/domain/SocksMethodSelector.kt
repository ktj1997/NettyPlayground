package com.example.netty.socks5.domain

class SocksMethodSelector {
    fun select(): SocksMethod {
        return SocksMethod.NO_AUTH
    }
}
