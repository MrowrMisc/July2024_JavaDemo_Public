package org.mrowrpurr.demo.game

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import java.net.URI

class Game : GameApplication() {
    private lateinit var wsClient: GameWebsocketClient

    override fun initSettings(settings: GameSettings) {
        settings.width = 800
        settings.height = 600
        settings.title = "Hello World"
    }

    override fun initGame() {
        println("Game is starting")
        wsClient = GameWebsocketClient(URI("ws://localhost:8080/ws"))
        wsClient.connect()
    }

    override fun initUI() {
        println("UI is starting")
    }

    override fun onUpdate(tpf: Double) {
        println("Game loop")
    }
}

fun main(args: Array<String>) {
    GameApplication.launch(Game::class.java, args)
}
