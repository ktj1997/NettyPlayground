package com.example.netty.integration

import com.example.netty.socks5.domain.SocksMethod
import com.example.netty.socks5.domain.SocksNegotiationRequest
import com.example.netty.socks5.domain.SocksNegotiationResponse
import com.example.netty.socks5.domain.SocksVersion
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class SocksNegotiationTest {
    private val socksServerPort = 1080

    @Test
    fun `negotiation_response를_응답할_수_있다`() {
        val expectVersion = SocksVersion.SOCKS5
        val expectMethod = SocksMethod.NO_AUTH

        TestSocket(socksServerPort).use {
            val request =
                SocksNegotiationRequest(
                    version = SocksVersion.SOCKS5,
                    methodCount = 2,
                    candidates =
                        listOf(
                            SocksMethod.NO_AUTH,
                            SocksMethod.USERNAME_PASSWORD,
                        ),
                )

            it.writer.write(request.toBytes())
            it.writer.flush()

            val actual = SocksNegotiationResponse.fromByte(it.reader.readNBytes(2))

            assertEquals(expectVersion, actual.version)
            assertEquals(expectMethod, actual.method)

            print(actual)
        }
    }

    @Test
    fun `negotiation_message가_분할되서_들어와도_응답할_수_있다`() {
        val expectVersion = SocksVersion.SOCKS5
        val expectMethod = SocksMethod.NO_AUTH

        TestSocket(socksServerPort).use {
            val request =
                SocksNegotiationRequest(
                    version = SocksVersion.SOCKS5,
                    methodCount = 2,
                    candidates =
                        listOf(
                            SocksMethod.NO_AUTH,
                            SocksMethod.USERNAME_PASSWORD,
                        ),
                )

            val requestBytes = request.toBytes()

            it.writer.write(requestBytes.copyOfRange(0, 2))
            it.writer.flush()
            Thread.sleep(1000)
            it.writer.write(requestBytes.copyOfRange(2, 4))
            it.writer.flush()

            val actual = SocksNegotiationResponse.fromByte(it.reader.readNBytes(2))

            assertEquals(expectVersion, actual.version)
            assertEquals(expectMethod, actual.method)

            print(actual)
        }
    }
}
