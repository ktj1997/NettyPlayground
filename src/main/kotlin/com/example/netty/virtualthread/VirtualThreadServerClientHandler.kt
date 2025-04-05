package com.example.netty.virtualthread

import com.example.netty.virtualthread.VirtualThreadTaskExecutorConfig.Companion.VIRTUAL_THREAD_TASK_EXECUTOR
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.task.TaskExecutor
import org.springframework.stereotype.Component
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.util.concurrent.CompletableFuture

@Component
class VirtualThreadServerClientHandler(
    @Qualifier(VIRTUAL_THREAD_TASK_EXECUTOR)
    private val executor: TaskExecutor,
) {
    fun handle(socket: Socket) {
        socket.use {
            val inputStream = it.getInputStream().buffered()
            val outputStream = it.getOutputStream().buffered()
            val echoFuture = executor.supplyAsync { echo(inputStream, outputStream) }
            val pingFuture = executor.supplyAsync { ping(outputStream) }

            CompletableFuture.anyOf(echoFuture, pingFuture).get()
        }
    }

    private fun echo(
        inputStream: InputStream,
        outputStream: OutputStream,
    ) {
        while (true) {
            val read = inputStream.bufferedReader().readLine().toByteArray()
            logger.info { "[VirtualThreadServerClientHandler][Echo][Sent]" }
            outputStream.writeAndFlush(read)
        }
    }

    private fun ping(outputStream: OutputStream) {
        val ping = "ping\n"

        while (true) {
            Thread.sleep(1000L)
            logger.info { "[VirtualThreadServerClientHandler][Ping][Sent]" }
            outputStream.writeAndFlush(ping.toByteArray())
        }
    }

    private fun OutputStream.writeAndFlush(byteArray: ByteArray)  {
        this.write(byteArray)
        this.flush()
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
