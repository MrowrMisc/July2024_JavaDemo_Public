package org.mrowrpurr.demo.websocketserver

import io.lettuce.core.api.StatefulRedisConnection
import org.springframework.stereotype.Service

@Service
class RedisService(private val connection: StatefulRedisConnection<String, String>) {
    fun setValue(key: String, value: String) {
        println("Setting value: $key = $value")
        val commands = connection.sync()
        commands.set(key, value)
    }

    fun getValue(key: String): String? {
        println("Getting value: $key")
        val commands = connection.sync()
        return commands.get(key)
    }
}