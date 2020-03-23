package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import components.Box2dBodyComponent
import components.CharacterComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.*

class CharacterControlSystem : IteratingSystem(allOf(
    CharacterComponent::class,
    Box2dBodyComponent::class).get(),10) {

  val npcMpr = mapperFor<CharacterComponent>()
  val bodyMpr = mapperFor<Box2dBodyComponent>()

  override fun processEntity(entity: Entity, deltaTime:Float) {
    val character = npcMpr[entity].character
    val body = bodyMpr[entity]!!.body
  }

  private fun moveFromTo(desiredPos: Vector2, body: Body) {
    body.linearVelocity = body.position.moveTowards(desiredPos, 5f)
  }
}

fun Vector2.directionalVelocity(velocity : Float) : Vector2 {
  return (vec2(0f,0f) - this).nor() * velocity
}

fun Vector2.moveTowards(target: Vector2, velocity: Float) : Vector2 {
  return (target - this).nor() * velocity
}