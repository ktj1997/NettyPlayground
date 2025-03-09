package com.example.netty.echo.server

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

class TcpEchoServer(
    private val port: Int,
    private val handlers: List<ChannelHandler>,
) : NettyServer {
    // bossGroup: ClientSocket Accept
    private val bossGroup = NioEventLoopGroup(1)

    // workerGroup: Handle businessLogic (default: CPU Core * 2)
    private val workerGroup = NioEventLoopGroup()
    private var channel: Channel? = null

    override fun start()  {
        logger.info { "[TcpEchoServer][Start] (port:$port)" }
        try {
            val bootStrap = ServerBootstrap()
            bootStrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .childOption(ChannelOption.SO_KEEPALIVE, true) // tcp keep-alive 옵션 enable
                .childHandler(
                    object : ChannelInitializer<SocketChannel>() {
                        override fun initChannel(ch: SocketChannel) {
                            val pipeLine = ch.pipeline()
                            handlers.forEach { pipeLine.addLast(it) }
                        }
                    },
                )

            val future = bootStrap.bind(port).sync()
            channel = future.channel()
        } catch (e: Exception) {
            logger.error { "[TcpEchoServer][Start][Fail] (port:$port, cause:${e.message})" }
            stop()
        }
    }

    override fun stop()  {
        logger.info { "[TcpEchoServer][Stop] (port:$port)" }
        channel?.close()?.sync()
        bossGroup.shutdownGracefully().sync()
        workerGroup.shutdownGracefully().sync()
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
