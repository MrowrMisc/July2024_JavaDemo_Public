package org.mrowrpurr.demo.game

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import java.util.UUID

// In the real world, this should come from like... secure config or something
const val GAME_SECRET = "Yj99#YCfG9QFjbh9s@H8Er"

class GameWebsocketClient(serverUri: URI) : WebSocketClient(serverUri) {
    private var wsIdentifier: UUID? = null

    override fun onOpen(handshakedata: ServerHandshake?) {
        println("Connected to server: $uri")

        val message = mutableMapOf<String, Any>()
        message["action"] = "identify"
        message["role"] = "game"
        message["secret"] = GAME_SECRET

        val messageText = jacksonObjectMapper().writeValueAsString(message)

        println("Sending message: $messageText")
        send(messageText)
    }

    override fun onMessage(message: String) {
        println("Received message: $message")
        val jsonMapper = jacksonObjectMapper()
        try {
            val json = jsonMapper.readValue<Map<String, Any>>(message)

            // Identifying the game, itself, this is the response
            if (wsIdentifier == null) {
                if (json.getOrDefault("status", "") == "OK" && json.getOrDefault("identifier", "") != "") {
                    wsIdentifier = UUID.fromString(json["identifier"] as String)
                    println("Received identifier: $wsIdentifier")
                } else {
                    println("Error identifying: $json")
                }
                return
            }

            // Else this should be from a valid player...
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