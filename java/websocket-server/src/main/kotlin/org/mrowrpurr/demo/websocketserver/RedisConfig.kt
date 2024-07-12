package org.mrowrpurr.demo.websocketserver

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedisConfig {
    @Bean
    fun redisClient(
        @Value("\${spring.data.redis.host}") host: String,
        @Value("\${spring.data.redis.port}") port: Int
    ): RedisClient {
        return RedisClient.create(RedisURI.create(host, port))
    }

    @Bean(destroyMethod = "close")
    fun redisConnection(client: RedisClient): StatefulRedisConnection<String, String> {
        return client.connect()
    }
}