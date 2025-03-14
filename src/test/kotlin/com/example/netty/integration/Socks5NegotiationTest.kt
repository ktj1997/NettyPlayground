package com.example.netty.integration

import com.example.netty.socks5.domain.Socks5Method
import com.example.netty.socks5.domain.Socks5NegotiationRequest
import com.example.netty.socks5.domain.Socks5NegotiationResponse
import com.example.netty.socks5.domain.SocksVersion
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class Socks5NegotiationTest {
    private val socksServerPort = 1080

    @Test
    fun `SocksVersion이_SOCK5가_아니라면_연결이_거부된다`() {
        TestSocket(socksServerPort).use {
            val request =
                Socks5NegotiationRequest(
                    version = SocksVersion.UNKNOWN,
                    methodCount = 2,
                    candidates =
                        listOf(
                            Socks5Method.NO_AUTH,
                            Socks5Method.USERNAME_PASSWORD,
                        ),
                )

            it.writer.write(request.toBytes())
            it.writer.flush()

            assertTrue(it.reader.read() == -1)
        }
    }

    @Test
    fun `negotiation_response를_응답할_수_있다`() {
        val expectVersion = SocksVersion.SOCKS5
        val expectMethod = Socks5Method.NO_AUTH

        TestSocket(socksServerPort).use {
            val request =
                Socks5NegotiationRequest(
                    version = expectVersion,
                    methodCount = 2,
                    candidates =
                        listOf(
                            Socks5Method.NO_AUTH,
                            Socks5Method.USERNAME_PASSWORD,
                        ),
                )

            it.writer.write(request.toBytes())
            it.writer.flush()

            val actual = Socks5NegotiationResponse.fromByte(it.reader.readNBytes(2))

            assertEquals(expectVersion, actual.version)
            assertEquals(expectMethod, actual.method)

            print(actual)
        }
    }

    @Test
    fun `negotiation_message가_분할되서_들어와도_응답할_수_있다`() {
        val expectVersion = SocksVersion.SOCKS5
        val expectMethod = Socks5Method.NO_AUTH

        TestSocket(socksServerPort).use {
            val request =
                Socks5NegotiationRequest(
                    version = expectVersion,
                    methodCount = 2,
                    candidates =
                        listOf(
                            Socks5Method.NO_AUTH,
                            Socks5Method.USERNAME_PASSWORD,
                        ),
                )

            val requestBytes = request.toBytes()

            it.writer.write(requestBytes.copyOfRange(0, 2))
            it.writer.flush()
            Thread.sleep(1000)
            it.writer.write(requestBytes.copyOfRange(2, 4))
            it.writer.flush()

            val actual = Socks5NegotiationResponse.fromByte(it.reader.readNBytes(2))

            assertEquals(expectVersion, actual.version)
            assertEquals(expectMethod, actual.method)

            print(actual)
        }
    }
}
