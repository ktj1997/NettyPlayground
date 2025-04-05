import com.example.netty.common.util.getNBytes
import com.example.netty.common.util.readNBytes
import com.example.netty.socks5.domain.Socks5AddressType
import com.example.netty.socks5.domain.Socks5CommandRequest
import com.example.netty.socks5.domain.Socks5CommandType
import com.example.netty.socks5.domain.Socks5Status
import com.example.netty.socks5.domain.SocksVersion
import com.example.netty.socks5.domain.command.Socks5CommandRequestAddressResolverFactory
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

class Socks5CommandRequestDecoder(
    private val addressResolverFactory: Socks5CommandRequestAddressResolverFactory,
) : ByteToMessageDecoder() {
    override fun decode(
        ctx: ChannelHandlerContext,
        input: ByteBuf,
        out: MutableList<Any>,
    ) {
        val channel = ctx.channel()
        val state = channel.attr(Socks5Status.KEY).get()

        if (state == Socks5Status.COMMAND) {
            if (input.readableBytes() < 5) {
                return
            }

            val minimumBytes = input.getNBytes(4)

            val version = SocksVersion.fromByte(minimumBytes[0])
            val commandType = Socks5CommandType.fromByte(minimumBytes[1])
            val reserved = minimumBytes[2]

            val addressType = Socks5AddressType.fromByte(minimumBytes[3])
            val resolver = addressResolverFactory.getResolver(addressType)

            val requiredBytes =
                when (addressType) {
                    Socks5AddressType.IPV4 -> 4 + 4 + 2
                    Socks5AddressType.IPV6 -> 4 + 16 + 2
                    Socks5AddressType.DOMAIN -> {
                        val domainLength = input.getByte(4).toInt()
                        5 + domainLength + 2
                    }
                    Socks5AddressType.UNKNOWN -> throw IllegalArgumentException("Unknown address type")
                }

            if (input.readableBytes() < requiredBytes) {
                return
            }

            val (address, port) = resolver.resolve(input.readNBytes(requiredBytes))

            out.add(
                Socks5CommandRequest(
                    version = version,
                    command = commandType,
                    reserved = reserved,
                    addressType = addressType,
                    dstAddress = address,
                    dstPort = port,
                ),
            )
        } else {
            ctx.write(input.retain())
        }
    }
}
