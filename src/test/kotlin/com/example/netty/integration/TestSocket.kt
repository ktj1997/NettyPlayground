package com.example.netty.integration

import java.net.Socket

class TestSocket(
    private val port: Int,
) : AutoCloseable {
    private val socket = Socket("127.0.0.1", port)
    val reader = socket.getInputStream().buffered()
    val writer = socket.getOutputStream()

    override fun close() {
        socket.close()
        reader.close()
        writer.close()
    }
}
