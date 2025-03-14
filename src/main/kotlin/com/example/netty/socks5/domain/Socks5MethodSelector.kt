package com.example.netty.socks5.domain

class Socks5MethodSelector {
    fun select(): Socks5Method {
        return Socks5Method.NO_AUTH
    }
}
