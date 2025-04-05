package com.example.netty.virtualthread

import org.slf4j.MDC
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskDecorator
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class VirtualThreadTaskExecutorConfig {
    @Bean(name = [VIRTUAL_THREAD_TASK_EXECUTOR])
    fun virtualThreadTaskExecutor(taskDecorator: TaskDecorator): TaskExecutor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 0
            queueCapacity = 0
            keepAliveSeconds = 0
            setAwaitTerminationSeconds(30)
            setTaskDecorator(taskDecorator)
            setThreadNamePrefix("virtual-thread-")
            setWaitForTasksToCompleteOnShutdown(true)
            setThreadFactory(Thread.ofVirtual().factory())
        }
    }

    @Bean
    fun mdcTaskDecorator(): TaskDecorator {
        return TaskDecorator { runnable ->
            Runnable {
                try {
                    val contextMap = MDC.getCopyOfContextMap()
                    if (contextMap != null) {
                        MDC.setContextMap(contextMap)
                    }
                    runnable.run()
                } finally {
                    MDC.clear()
                }
            }
        }
    }

    companion object {
        const val VIRTUAL_THREAD_TASK_EXECUTOR = "virtualThreadTaskExecutor"
    }
}
