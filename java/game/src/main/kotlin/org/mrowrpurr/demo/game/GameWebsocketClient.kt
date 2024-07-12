package org.mrowrpurr.demo.game

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
        val jsonMapper = jacksonObjectMapper()
        try {
            val json = jsonMapper.readValue<Map<String, Any>>(message)
            val action = json["action"] as String
            println("Parsed Action: $action")
        } catch (e: JsonParseException) {
            println("Error parsing JSON: $e")
        } catch (e: JsonMappingException) {
            println("Error mapping JSON: $e")
        } catch (e: Exception) {
            println("Error parsing JSON: $e")
        }
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("Connection closed: $code, $reason, $remote")
    }

    override fun onError(ex: Exception?) {
        println("!!! here !!! Error: $ex")
    }

    override fun connect() {
        println("Connecting to server: $uri")
        super.connect()
    }
}