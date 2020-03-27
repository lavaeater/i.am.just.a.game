package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import components.Box2dBodyComponent
import components.NpcComponent
import components.RobbingComponent
import data.States
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.minus
import ktx.math.times
import ktx.math.vec2

class NpcControlSystem : IteratingSystem(allOf(
    NpcComponent::class,
    Box2dBodyComponent::class).get(),10) {

  private val npcMpr = mapperFor<NpcComponent>()
  private val bodyMpr = mapperFor<Box2dBodyComponent>()
  private val robMapper = mapperFor<RobbingComponent>()

  override fun processEntity(entity: Entity, deltaTime: Float) {
    val npc = npcMpr[entity].npc

    val body = bodyMpr[entity]!!.body
    if (npc.npcState == States.OnTheMove) {
      npc.thisIsWhereIWantToBe.moveFromTo(body)

      if (npc.thisIsWhereIWantToBe.dst(body.position) < 3f)
        npc.stopDoingIt()
    }
    if(npc.npcState != States.OnTheMove)
      body.linearVelocity = vec2(0f,0f)

    if(npc.isDead)
      engine.removeEntity(entity)
  }
}

fun Body.isWithin(radius:Float, body:Body) : Boolean {
  return this.position.dst(body.position) < radius
}

fun Vector2.moveFromTo(body:Body) {
  body.linearVelocity = body.position.moveTowards(this, 10f)
}

fun Body.moveTowards(body:Body) {
  this.linearVelocity = this.position.moveTowards(body.position, 6f)
}

fun Vector2.directionalVelocity(velocity : Float) : Vector2 {
  return (vec2(0f,0f) - this).nor() * velocity
}

fun Vector2.moveTowards(target: Vector2, velocity: Float) : Vector2 {
  return (target - this).nor() * velocity
}