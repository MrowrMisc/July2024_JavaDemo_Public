package org.mrowrpurr.demo.websocketserver

import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

class WebsocketMessageHandler : TextWebSocketHandler() {
    private val sessions = mutableListOf<WebSocketSession>()

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("Received message: ${message.payload}")

        // Send the message payload to ALL connected clients:
        sessions.forEach { it.sendMessage(TextMessage("We received a message: ${message.payload}")) }
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