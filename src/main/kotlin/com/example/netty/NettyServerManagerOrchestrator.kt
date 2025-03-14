package com.example.netty

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Component

@Component
class NettyServerManagerOrchestrator(
    private val serverManagers: List<NettyServerManager>,
) {
    @PostConstruct
    fun init() {
        serverManagers.forEach { manager ->
            manager.startAll()
        }
    }

    @PreDestroy
    fun clean() {
        serverManagers.forEach { manager ->
            manager.stopAll()
        }
    }
}
