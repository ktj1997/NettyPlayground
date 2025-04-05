package com.example.netty.socks5.domain.command

import com.example.netty.socks5.domain.Socks5AddressType

class Socks5CommandRequestAddressResolverFactory {
    fun getResolver(addressType: Socks5AddressType): Socks5CommandRequestAddressResolver {
        return when (addressType) {
            Socks5AddressType.IPV4 -> Socks5CommandRequestIpV4AddressResolver()
            Socks5AddressType.DOMAIN -> Socks5CommandRequestDomainAddressResolver()
            Socks5AddressType.IPV6 -> Socks5CommandRequestIpV6AddressResolver()
            Socks5AddressType.UNKNOWN -> throw IllegalArgumentException("Unknown address type")
        }
    }
}
