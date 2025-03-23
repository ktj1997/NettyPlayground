import com.example.netty.socks5.domain.command.Socks5CommandRequestIpV4AddressResolver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Socks5CommandRequestIpV4AddressResolverTest {
    @Test
    fun `test IPv4 address resolver`() {
        val payload =
            byteArrayOf(
                0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
                192.toByte(), 168.toByte(), 1.toByte(), 1.toByte(),
                0x1F.toByte(), 0x90.toByte(),
            )
        val resolver = Socks5CommandRequestIpV4AddressResolver()
        val (address, port) = resolver.resolve(payload)
        assertEquals("192.168.1.1", address)
        assertEquals(8080, port)
    }

    @Test
    fun `test IPv4 address resolver with boundary values`() {
        val payload =
            byteArrayOf(
                0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
                255.toByte(), 255.toByte(), 255.toByte(), 255.toByte(),
                0xFF.toByte(), 0xFF.toByte(),
            )
        val resolver = Socks5CommandRequestIpV4AddressResolver()
        val (address, port) = resolver.resolve(payload)
        assertEquals("255.255.255.255", address)
        assertEquals(65535, port)
    }
}
