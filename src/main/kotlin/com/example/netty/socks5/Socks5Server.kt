package com.example.netty.socks5

import com.example.netty.NettyServer
import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

class Socks5Server(
    private val port: Int,
    private val handlers: () -> List<ChannelHandler>,
) : NettyServer {
    private val bossGroup = NioEventLoopGroup(1)
    private val workerGroup = NioEventLoopGroup()
    private var channel: Channel? = null

    override fun start() {
        logger.info { "[Socks5Server][Start] (port:$port)" }
        try {
            val bootStrap = ServerBootstrap()
            bootStrap.group(bossGroup)
                .channel(NioServerSocketChannel::class.java)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(
                    object : ChannelInitializer<SocketChannel>() {
                        override fun initChannel(ch: SocketChannel) {
                            val pipeLine = ch.pipeline()
                            handlers().forEach { pipeLine.addLast(it) }
                        }
                    },
                )

            channel = bootStrap.bind(port).sync().channel()
        } catch (e: Exception) {
            logger.error { "[Socks5Server][Start][Fail] (port:$port, cause:${e.message})" }
            stop()
        }
    }

    override fun stop() {
        logger.info { "[Socks5Server][Stop] (port:$port)" }
        channel?.close()?.sync()
        bossGroup.shutdownGracefully().sync()
        workerGroup.shutdownGracefully().sync()
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
