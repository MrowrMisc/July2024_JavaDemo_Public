package org.mrowrpurr.demo.websocketserver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

// TODO... when a NEW SESSION connects... track it as an UNKNOWN type... THEN listen for a message that identifies it... ...

class WebsocketMessageHandler(private val redis: RedisService) : TextWebSocketHandler() {

    init {
        println("WebsocketMessageHandler created")
        println("Can we talk to redis?")
        redis.setValue("test", "value")
    }

    // TODO: Admin GUI and Game should identify themselves (using secrets)
    //       and we should treat all the other connections as clients (and not trust them! or broadcast to them)

//    public val sessionIds: Map<>
    private val sessions = mutableListOf<WebSocketSession>()

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("Received message: ${message.payload}")
        sessions.forEach { it.sendMessage(TextMessage(message.payload)) }
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
}