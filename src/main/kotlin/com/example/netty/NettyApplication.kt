package com.example.netty

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class NettyApplication

fun main(args: Array<String>) {
    runApplication<NettyApplication>(*args)
}
