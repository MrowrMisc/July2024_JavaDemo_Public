package org.mrowrpurr.demo.websocketserver

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer

@Configuration
class RedisPubSubConfig(private val redisConnectionFactory: RedisConnectionFactory) {

    @Bean
    fun messageListenerContainer(subscriber: RedisPubSubSubscriber): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(redisConnectionFactory)
        container.addMessageListener(subscriber, ChannelTopic("game")) // TODO read from config
        return container
    }
}