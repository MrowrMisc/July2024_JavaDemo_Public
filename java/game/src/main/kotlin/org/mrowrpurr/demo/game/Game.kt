package org.mrowrpurr.demo.game

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.input.UserAction
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import org.mrowrpurr.demo.game.components.PlayerComponent
import java.net.URI
import java.util.UUID

class Game : GameApplication() {
    private lateinit var wsClient: GameWebsocketClient
//    private lateinit var firstEntity: Entity

    override fun initSettings(settings: GameSettings) {
        settings.width = 1920
        settings.height = 1080
        settings.title = "Demo Game"
    }

    override fun initGame() {
        println("Game is starting...")

        // TODO: (1) do we need this? (2) should we change thickness?
        entityBuilder().buildScreenBoundsAndAttach(100.0)

        getPhysicsWorld().setGravity(0.0, 800.0)
        getGameWorld().addEntityFactory(GameEntityFactory())

        // TODO HERE HARD CODED :)
//        wsClient = GameWebsocketClient(URI("ws://localhost:8080/ws"))
        wsClient = GameWebsocketClient(URI("wss://websocketsagain-n5mpyq257a-uw.a.run.app/ws"))

//        wsClient.registerPlayerActionCallback(::handlePlayerAction)
        wsClient.registerPlayerActionCallback { identifier, action, data ->
            Platform.runLater {
                handlePlayerAction(identifier, action, data)
            }
        }
        wsClient.connect()
    }

    private fun handlePlayerAction(identifier: UUID, action: String, data: Map<*, *>) {
        println("Handling player action: $action from ID: $identifier - data: $data")
        when (action) {
            "addPlayer" -> addPlayerAction(identifier, data)
            "removePlayer" -> removePlayerAction(identifier, data)
            "movePlayer" -> movePlayerAction(identifier, data)
            else -> println("Unknown action: $action")
        }
    }

    private val uuidsToEntities = mutableMapOf<UUID, Entity>()

    private fun addPlayerAction(identifier: UUID, data: Map<*, *>) {
        if (uuidsToEntities.containsKey(identifier)) {
            println("Player already exists: $identifier")
            return
        }

        val playerName = data.getOrDefault("name", null) as String?
        if (playerName == null) {
            println("Player name not provided: $identifier")
            return
        }

        var playerColorHex = data.getOrDefault("color", null) as String?
        if (playerColorHex == null) {
            println("Player color not provided: $identifier")
            return
        }
        if (!playerColorHex.startsWith("#")) playerColorHex = "#$playerColorHex"

        val playerEntity = spawn("player", SpawnData().put("paint", Color.web(playerColorHex)).put("name", playerName))

        // TODO: select a starting position, from the top, moving across and then repeating...
        // For now, static:
        playerEntity.transformComponent.setPosition(100.0, 100.0)

        uuidsToEntities[identifier] = playerEntity
    }

    private fun removePlayerAction(identifier: UUID, data: Map<*, *>) {
        if (!uuidsToEntities.containsKey(identifier)) {
            println("Player does not exist: $identifier")
            return
        }
        getGameWorld().removeEntity(uuidsToEntities[identifier]!!)
    }

    private fun movePlayerAction(identifier: UUID, data: Map<*, *>) {
        if (!uuidsToEntities.containsKey(identifier)) {
            println("Player does not exist: $identifier")
            return
        }

        val direction = data.getOrDefault("direction", null) as String?
        if (direction == null) {
            println("Direction not provided: $identifier")
            return
        }

        val entity = uuidsToEntities[identifier]!!

        when (direction) {
            "up" -> entity.getComponent(PlayerComponent::class.java).jump()
            "left" -> entity.getComponent(PlayerComponent::class.java).left()
            "right" -> entity.getComponent(PlayerComponent::class.java).right()
            else -> println("Unknown direction: $direction")
        }
    }
}

//fun main(args: Array<String>) {
//    GameApplication.launch(Game::class.java, args)
//}

class EmbeddedFXGLApp : Application() {
    override fun start(primaryStage: Stage) {
        val gameApp = Game()

        val fxglPane = GameApplication.embeddedLaunch(gameApp)
        fxglPane.renderFill = Color.TRANSPARENT // Required!

        val root = StackPane(fxglPane)
        root.style = "-fx-background-color: transparent;" // Required!

        val scene = Scene(root, 1920.0, 1080.0, Color.TRANSPARENT)

        primaryStage.initStyle(StageStyle.TRANSPARENT) // Required!
        primaryStage.scene = scene
        primaryStage.show()
    }
}

fun main() {
    Application.launch(EmbeddedFXGLApp::class.java)
}

