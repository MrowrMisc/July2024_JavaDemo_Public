package org.mrowrpurr.demo.game

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings

class Game : GameApplication() {
    override fun initSettings(settings: GameSettings) {
        settings.width = 800
        settings.height = 600
        settings.title = "Hello World"
    }

    override fun initGame() {
        println("Game is starting")
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
