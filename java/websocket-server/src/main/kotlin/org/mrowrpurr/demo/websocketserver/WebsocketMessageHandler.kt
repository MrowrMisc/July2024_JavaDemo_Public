package org.mrowrpurr.demo.websocketserver

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.UUID

// Hardcoded secret hashes
const val GAME_SECRET_HASH = "8956fd095458cfcaa4a6dec37a40e0a77707873f9ba4d74c543d0cefd54fa73f"
const val ADMIN_SECRET_HASH = "7142fcfeffd3efc8d0973206990ab217b01092e05276d587d5d394529b89f47e"
const val PLAYER_SECRET_HASH = "1b777c99f56929656c547a229d29dccd888ccf19a9531ab18454ab30939936ab"

@Service
class WebsocketMessageHandler(
    private val redis: RedisService,
    private val pubsub: StringRedisTemplate,
    private val lastMessageSent: LastMessageSent,
    private val jacksonObjectMapper: ObjectMapper
) : TextWebSocketHandler() {

    private val uuidsToVerifiedRoles = mutableMapOf<UUID, String>()

    private var gameSession: WebSocketSession? = null
    private var adminSession: WebSocketSession? = null
    private val sessions = mutableListOf<WebSocketSession>()

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("[MessageHandler] Received text message: ${message.payload}")
        lastMessageSent.lastMessageSent = message.payload
        println("[MessageHandler] Publishing message to Redis: ${message.payload}")
        pubsub.convertAndSend("game", message.payload)
        println("[MessageHandler] Attempting to process message: ${message.payload}")
        processMessageString(session, message.payload)
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

    private fun processMessageString(session: WebSocketSession, message: String) {
        println("[MessageHandler] Processing message: $message")
        try {
            val jsonMapper = jacksonObjectMapper()
            val json: Map<String, Any> = jsonMapper.readValue(message)
            processMessageMap(session, json)
        } catch (e: JsonParseException) {
            println("[MessageHandler] Error parsing JSON: $e")
        } catch (e: JsonMappingException) {
            println("[MessageHandler] Error mapping JSON: $e")
        } catch (e: Exception) {
            println("[MessageHandler] Error processing message: $e")
        }
    }

    private fun processMessageMap(session: WebSocketSession, message: Map<String, Any>) {
        println("[MessageHandler] Processing message: $message")

        val identifier = message.getOrDefault("identifier", null) as String?
        val action = message.getOrDefault("action", null) as String?

        if (identifier == null) {
            if (action == "identify") actionIdentify(session, message)
            else println("[MessageHandler] Message from unidentified source: $message")
            return
        }

        val uuid = UUID.fromString(identifier)
        if (!uuidsToVerifiedRoles.containsKey(uuid)) {
            println("[MessageHandler] Unverified identifier: $identifier")
            return
        }

        val role = uuidsToVerifiedRoles[uuid]
        if (role == "player") {
            if (gameSession == null) {
                println("[MessageHandler] Game session not established")
                return
            } else {
                println("[MessageHandler] Sending message to game session...")
                gameSession!!.sendMessage(TextMessage(jacksonObjectMapper.writeValueAsString(message)))
            }
            if (adminSession == null) {
                println("[MessageHandler] Admin session not established")
                return
            } else {
                println("[MessageHandler] Sending message to admin session...")
                adminSession!!.sendMessage(TextMessage(jacksonObjectMapper.writeValueAsString(message)))
            }
        }
    }

    private fun createNewIdentityAndReply(session: WebSocketSession, role: String) {
        val uuid = UUID.randomUUID()
        uuidsToVerifiedRoles[uuid] = role
        val response = mutableMapOf<String, Any>()
        response["status"] = "OK"
        response["identifier"] = uuid.toString()
        session.sendMessage(TextMessage(jacksonObjectMapper.writeValueAsString(response)))
    }

    private fun actionIdentify(session: WebSocketSession, data: Map<String, Any>) {
        println("[MessageHandler] Identifying: $data")

        val role = data.getOrDefault("role", null) as String?
        val secret = data.getOrDefault("secret", null) as String?

        if (role == null) {
            println("[MessageHandler] No role provided")
            return
        }

        if (secret == null) {
            println("[MessageHandler] No secret provided")
            return
        }

        val secretHash = DigestUtils.sha256Hex(secret).toString()

        when (role) {
            "game" -> {
                if (secretHash == GAME_SECRET_HASH) {
                    println("[MessageHandler] Identified as game")
                    gameSession = session
                    createNewIdentityAndReply(session, role)
                } else {
                    println("[MessageHandler] Invalid game secret")
                }
            }
            "admin" -> {
                if (secretHash == ADMIN_SECRET_HASH) {
                    println("[MessageHandler] Identified as admin")
                    adminSession = session
                    createNewIdentityAndReply(session, role)
                } else {
                    println("[MessageHandler] Invalid admin secret")
                }
            }
            "player" -> {
                if (secretHash == PLAYER_SECRET_HASH) {
                    println("[MessageHandler] Identified as player")
                    createNewIdentityAndReply(session, role)
                } else {
                    println("[MessageHandler] Invalid player secret")
                }
            }
            else -> {
                println("[MessageHandler] Invalid role: $role")
            }
        }
    }
}