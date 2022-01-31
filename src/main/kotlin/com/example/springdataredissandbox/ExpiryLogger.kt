package com.example.springdataredissandbox

import org.springframework.context.event.EventListener
import org.springframework.data.redis.core.RedisKeyExpiredEvent
import org.springframework.stereotype.Component

@Component
class ExpiryLogger {

    @EventListener
    fun logExpiredData(event: RedisKeyExpiredEvent<*>) {
        println("event = [${event}]")
    }
}
