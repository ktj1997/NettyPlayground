package com.example.netty.virtualthread

import com.example.netty.virtualthread.VirtualThreadTaskExecutorConfig.Companion.VIRTUAL_THREAD_TASK_EXECUTOR
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.task.TaskExecutor
import org.springframework.stereotype.Component

@Component
class VirtualThreadServerStarter(
    @Qualifier(VIRTUAL_THREAD_TASK_EXECUTOR)
    private val taskExecutor: TaskExecutor,
    private val clientHandler: VirtualThreadServerClientHandler,
) {
    private lateinit var server: VirtualThreadTcpServer

    @PostConstruct
    fun init() {
        server = VirtualThreadTcpServer(1000, taskExecutor, clientHandler)
        server.start()
    }

    @PreDestroy
    fun close() {
        server.close()
    }
}
