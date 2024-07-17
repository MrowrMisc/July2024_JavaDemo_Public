package org.mrowrpurr.demo.game

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.entity.components.CollidableComponent
import com.almasb.fxgl.entity.components.IrremovableComponent
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.almasb.fxgl.physics.PhysicsComponent
import com.almasb.fxgl.physics.box2d.dynamics.BodyType
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import org.mrowrpurr.demo.game.components.PlayerComponent

class GameEntityFactory : EntityFactory {
    @Spawns("player")
    fun newPlayer(data: SpawnData): Entity {
        val physics = PhysicsComponent()
        physics.setBodyType(BodyType.DYNAMIC)
        physics.addGroundSensor(HitBox("GROUND_SENSOR", Point2D(16.0, 38.0), BoundingShape.box(6.0, 8.0)))

        // this avoids the player from sticking to walls
        physics.setFixtureDef(FixtureDef().friction(0.0f))

        val paint = data.get<Paint?>("paint") ?: Color.MAGENTA
        val name = data.get<String?>("name") ?: "Player"
        val playerComponent = PlayerComponent(name, paint)

        // Hmmm... figure out injection!
        playerComponent.physics = physics

        return entityBuilder()
            .type(EntityType.PLAYER)
            .bbox(HitBox(Point2D(5.0, 5.0), BoundingShape.box(100.0, 100.0)))
//            .viewWithBBox(playerComponent.rectangle)
            .view(playerComponent.rectangle)
            .with(playerComponent)
            .with(physics)
            .with(CollidableComponent(true))
            .with(IrremovableComponent())
//            .with(playerComponent)
//            .with(PlayerComponent(name, paint))
            .build()
    }
}