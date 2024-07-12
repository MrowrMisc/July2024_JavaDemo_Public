package org.mrowrpurr.demo.websocketserver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

// 1. publish incoming messages to a redis topic
// 2. listen for messages on a redis topic and broadcast them to all connected clients

// ----------------------------

// TODO... when a NEW SESSION connects... track it as an UNKNOWN type... THEN listen for a message that identifies it... ...

@Service
class WebsocketMessageHandler(
    private val redis: RedisService,
    private val pubsub: StringRedisTemplate,
    private val lastMessageSent: LastMessageSent
) : TextWebSocketHandler() {

        init { println("WebsocketMessageHandler created") }

    // TODO: Admin GUI and Game should identify themselves (using secrets)
    //       and we should treat all the other connections as clients (and not trust them! or broadcast to them)

//    public val sessionIds: Map<>
    private val sessions = mutableListOf<WebSocketSession>()

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("[MessageHandler] Received text message: ${message.payload}")

        // Let the Subscriber know that we DON'T want to publish our OWN message...
        lastMessageSent.lastMessageSent = message.payload

        // Publish!
        println("[MessageHandler] Publishing message to Redis: ${message.payload}")
        pubsub.convertAndSend("game", message.payload)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)
        println("Connection established")
        sessions.add(session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        super.afterConnectionClosed(session, status)
        println("Connection closed")
        sessions.remove(session)
    }

    fun broadcastMessage(message: String) {
        println("[MessageHandler] Broadcasting message: $message")
        sessions.forEach { it.sendMessage(TextMessage(message)) }
    }
}