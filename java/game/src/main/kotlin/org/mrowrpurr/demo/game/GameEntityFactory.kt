package org.mrowrpurr.demo.game

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.physics.PhysicsComponent
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import org.mrowrpurr.demo.game.components.PlayerComponent

class GameEntityFactory : EntityFactory {
    @Spawns("player")
    fun newPlayer(data: SpawnData): Entity {
        val paint = data.get<Paint?>("paint") ?: Color.MAGENTA
        val playerComponent = PlayerComponent(paint)
        return entityBuilder()
            .type(EntityType.PLAYER)
            .viewWithBBox(playerComponent.rectangle)
            .with(playerComponent)
            .build()
    }
}