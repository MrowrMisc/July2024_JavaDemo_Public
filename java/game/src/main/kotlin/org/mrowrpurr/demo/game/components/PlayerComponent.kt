package org.mrowrpurr.demo.game.components

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.physics.PhysicsComponent
import javafx.scene.control.skin.TextInputControlSkin.Direction
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.Text

class PlayerComponent(private val name: String, private val paint: Paint = Color.MAGENTA, private val size: Double = 100.0, private val velocity: Double = 5.0) : Component() {
    var currentDirection: Direction = Direction.RIGHT
    var rectangle: Rectangle = Rectangle(size, size, paint)
    private val text: Text = Text(name)
    lateinit var physics: PhysicsComponent

    override fun onAdded() {
        text.font = Font(30.0)
        text.fill = Color.WHITE
        entity.viewComponent.addChild(text)
    }

    override fun onRemoved() { entity.viewComponent.removeChild(text); }

    override fun onUpdate(tpf: Double) {
        text.translateX = rectangle.translateX
        text.translateY = rectangle.translateY
    }

    fun stop() { physics.velocityX = 0.0 }

    fun left() {
        if (currentDirection == Direction.RIGHT) {
            currentDirection = Direction.LEFT
            stop()
        } else {
            entity.translateX(-velocity)
            physics.velocityX = -150.0
        }
    }

    fun right() {
        if (currentDirection == Direction.LEFT) {
            currentDirection = Direction.RIGHT
            stop()
        } else {
            entity.translateX(velocity)
            physics.velocityX = 150.0
        }
    }

    fun jump() {
        if (physics.isOnGround) {
            entity.translateY(-velocity)
            physics.velocityY = -500.0
        }
    }
}