package com.example.netty.integration

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.net.Socket
import kotlin.test.assertEquals

@SpringBootTest
class TcpEchoServerTest {
    @Test
    fun `Client는_요청을_Echo로_응답_받을_수_있다`() {
        val expect = "hello"
        val client = Socket("127.0.0.1", 3000)

        client.use {
            it.getOutputStream().write(expect.toByteArray())
            val actual = it.getInputStream().readNBytes(expect.length)

            assertEquals(expect, String(actual))
        }
    }
}
