package com.example.netty.virtualthread

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

fun <T> Executor.supplyAsync(block: () -> T): CompletableFuture<T> {
    return CompletableFuture.supplyAsync(block, this)
}
