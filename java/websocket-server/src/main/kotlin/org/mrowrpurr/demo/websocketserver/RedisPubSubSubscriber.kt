package org.mrowrpurr.demo.websocketserver

import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Service

@Service
class RedisPubSubSubscriber(private val webSocketHandler: WebsocketMessageHandler, private val lastMessageSent: LastMessageSent) : MessageListener {
    override fun onMessage(message: org.springframework.data.redis.connection.Message, pattern: ByteArray?) {
        val messageBody = String(message.body)
        println("[Subscriber] Received message: $messageBody")

        if (messageBody == lastMessageSent.lastMessageSent) {
            println("[Subscriber] Ignoring message: $messageBody")
        } else {
            println("[Subscriber] Broadcasting message: $messageBody")
            webSocketHandler.broadcastMessage(messageBody)
        }
    }
}

