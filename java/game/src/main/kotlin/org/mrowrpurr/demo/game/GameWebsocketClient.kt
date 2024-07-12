package org.mrowrpurr.demo.game

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class GameWebsocketClient(serverUri: URI) : WebSocketClient(serverUri) {
    override fun onOpen(handshakedata: ServerHandshake?) {
        println("Connected to server: $uri")
    }

    override fun onMessage(message: String) {
        println("Received message: $message")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("Connection closed: $code, $reason, $remote")
    }

    override fun onError(ex: Exception?) {
        println("Error: $ex")
    }

    override fun connect() {
        println("Connecting to server: $uri")
        super.connect()
    }
}