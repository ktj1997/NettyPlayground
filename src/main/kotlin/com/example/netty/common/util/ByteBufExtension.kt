package com.example.netty.common.util

import io.netty.buffer.ByteBuf

fun ByteBuf.getNBytes(n: Int): ByteArray {
    val bytes = ByteArray(n)
    getBytes(readerIndex(), bytes, 0, n)

    return bytes
}

fun ByteBuf.readNBytes(n: Int): ByteArray {
    val bytes = ByteArray(n)
    readBytes(bytes, 0, n)

    return bytes
}
