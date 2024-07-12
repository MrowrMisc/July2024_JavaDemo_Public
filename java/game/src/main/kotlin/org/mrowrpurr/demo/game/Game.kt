package org.mrowrpurr.demo.game

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.dsl.spawn
import com.almasb.fxgl.entity.SpawnData
import javafx.scene.paint.Color
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
        getGameWorld().addEntityFactory(GameEntityFactory())

        // Test adding some characters!
        val player1 = spawn("player", SpawnData().put("paint", Color.BLUE).put("name", "Ralph"))
        val player2 = spawn("player", SpawnData().put("paint", Color.RED).put("name", "Alice"))

        player1.transformComponent.setPosition(100.0, 100.0)
        player2.transformComponent.setPosition(200.0, 200.0)

        wsClient = GameWebsocketClient(URI("ws://localhost:8080/ws"))
        wsClient.connect()
    }

    override fun initUI() {
        println("UI is starting")
    }
}

fun main(args: Array<String>) {
    GameApplication.launch(Game::class.java, args)
}
