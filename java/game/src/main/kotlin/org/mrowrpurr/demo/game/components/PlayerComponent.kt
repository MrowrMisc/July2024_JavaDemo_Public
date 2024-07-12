package org.mrowrpurr.demo.game.components

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.physics.PhysicsComponent
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text

class PlayerComponent(private val name: String, private val paint: Paint = Color.MAGENTA, private val size: Double = 50.0, private val velocity: Double = 5.0) : Component() {
    var rectangle: Rectangle = Rectangle(size, size, paint)
    private val text: Text = Text(name)
    lateinit var physics: PhysicsComponent

    override fun onAdded() { entity.viewComponent.addChild(text) }
    override fun onRemoved() { entity.viewComponent.removeChild(text); }

    override fun onUpdate(tpf: Double) {
        text.translateX = rectangle.translateX
        text.translateY = rectangle.translateY
    }

    fun left() { entity.translateX(-velocity); physics.velocityX = -150.0 }
    fun right() { entity.translateX(velocity); physics.velocityX = 150.0 }

    fun jump() {
        if (physics.isOnGround) {
            entity.translateY(-velocity)
            physics.velocityY = -300.0
        }
    }
}