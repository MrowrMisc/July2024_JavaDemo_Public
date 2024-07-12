package org.mrowrpurr.demo.game.components

import com.almasb.fxgl.entity.component.Component
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle

class PlayerComponent(private val paint: Paint = Color.MAGENTA, private val size: Double = 50.0, private val velocity: Double = 5.0) : Component() {
    var rectangle: Rectangle = Rectangle(size, size, paint)

    fun left() { entity.translateX(-velocity) }
    fun right() { entity.translateX(velocity) }
}