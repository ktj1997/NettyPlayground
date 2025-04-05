package com.example.netty.virtualthread

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.task.TaskExecutor
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

class VirtualThreadTcpServer(
    private val port: Int,
    private val workers: TaskExecutor,
    private val clientHandler: VirtualThreadServerClientHandler,
) : AutoCloseable {
    private val clients: ConcurrentHashMap<String, Socket> = ConcurrentHashMap()
    private val boss = Executors.newSingleThreadExecutor()

    @Volatile
    private var isRunning = true
    private var serverSocket: ServerSocket? = null

    fun start() {
        logger.info { "[VirtualThreadTcpServer][Start] (port: $port)" }

        serverSocket = ServerSocket(port)
        boss.supplyAsync {
            while (isRunning) {
                val clientSocket = serverSocket!!.accept()
                clients[clientSocket.id()] = clientSocket
                workers.supplyAsync {
                    clientHandler.handle(clientSocket)
                }.exceptionally {
                    logger.error(it) { "[VirtualThreadServerClientHandler][Error] (id: ${clientSocket.id()})" }
                }
            }
        }.exceptionally {
            logger.error(it) { "[VirtualThreadTcpServer][Error] accept fail" }
        }
    }

    override fun close() {
        isRunning = false
        serverSocket?.close()
        clients.values.forEach { it.close() }
        clients.clear()
        boss.shutdown()
        logger.info { "[VirtualThreadTcpServer][Close] (port: $port)" }
    }

    private fun Socket.id(): String {
        val src = "${this.localAddress.hostName}:${this.localPort}"
        val dest = "${this.inetAddress.hostName}:${this.port}"

        return "${src}_$dest"
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
