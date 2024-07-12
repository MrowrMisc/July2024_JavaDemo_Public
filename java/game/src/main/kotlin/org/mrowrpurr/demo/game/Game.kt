package org.mrowrpurr.demo.game

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.input.UserAction
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import org.mrowrpurr.demo.game.components.PlayerComponent
import java.net.URI

class Game : GameApplication() {
    private lateinit var wsClient: GameWebsocketClient
//    private lateinit var firstEntity: Entity

    override fun initSettings(settings: GameSettings) {
        settings.width = 800
        settings.height = 600
        settings.title = "Hello World"
    }

    override fun initGame() {
        println("Game is starting")

        // TODO: (1) do we need this? (2) should we change thickness?
        entityBuilder().buildScreenBoundsAndAttach(20.0)

        getPhysicsWorld().setGravity(0.0, 800.0)

        getGameWorld().addEntityFactory(GameEntityFactory())

        // Test adding some characters!
//        val player1 = spawn("player", SpawnData().put("paint", Color.BLUE).put("name", "Ralph"))
//        val player2 = spawn("player", SpawnData().put("paint", Color.RED).put("name", "Alice"))

//        player1.transformComponent.setPosition(100.0, 100.0)
//        player2.transformComponent.setPosition(200.0, 200.0)

//        firstEntity = player1

        wsClient = GameWebsocketClient(URI("ws://localhost:8080/ws"))
        wsClient.connect()
    }

//    override fun initUI() {
//        println("UI is starting")
//    }

//    override fun initInput() {
//        val rightInput = object : UserAction("Move Right") {
//            override fun onAction() { firstEntity.getComponent(PlayerComponent::class.java).right() }
//        }
//        val leftInput = object : UserAction("Move Left") {
//            override fun onAction() { firstEntity.getComponent(PlayerComponent::class.java).left() }
//        }
//        val upInput = object : UserAction("Move Up") {
//            override fun onAction() { firstEntity.getComponent(PlayerComponent::class.java).jump() }
//        }
//        getInput().addAction(rightInput, KeyCode.D)
//        getInput().addAction(leftInput, KeyCode.A)
//        getInput().addAction(upInput, KeyCode.SPACE)
//    }
}

fun main(args: Array<String>) {
    GameApplication.launch(Game::class.java, args)
}
