package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import components.Box2dBodyComponent
import components.NpcComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.minus
import ktx.math.times
import ktx.math.vec2

/**
 * A person walks a klick per 10 minutes - meaning that every hour is 6 klicks.
 *
 * 6000
 * this is 1.6 meters / second. Which is what our speed should be!
 *
 * r
 */
class NpcControlSystem : IteratingSystem(allOf(
    NpcComponent::class,
    Box2dBodyComponent::class).get()) {

  private val npcMpr = mapperFor<NpcComponent>()
  private val bodyMpr = mapperFor<Box2dBodyComponent>()
  private var someVector = vec2(0f, 0f)

  private var somekindOfSpeedFactor = 2f * AiAndTimeSystem.secondsPerSecond

  override fun processEntity(entity: Entity, deltaTime: Float) {
    val npc = npcMpr[entity].npc

    val body = bodyMpr[entity]!!.body
    if (npc.onTheMove) {
      if (npc.meetingAFriend) {
        npc.friendToGoTo?.currentPosition?.moveFromTo(body, somekindOfSpeedFactor)
        if (npc.circleOfConcern.contains(npc.friendToGoTo?.currentPosition))
          npc.stopDoingIt()
      } else {
        npc.thePlaceIWantToBe.box.getCenter(someVector).moveFromTo(body, somekindOfSpeedFactor)
        if (npc.thePlaceIWantToBe.box.contains(npc.currentPosition))
          npc.stopDoingIt()
      }
    }
    npc.currentPosition = body.position

    if (!npc.onTheMove)
      body.linearVelocity = vec2(0f, 0f)
  }
}


fun Body.isWithin(radius:Float, body:Body) : Boolean {
  return this.position.dst(body.position) < radius
}

fun Vector2.moveFromTo(body:Body, velocity: Float) {
  body.linearVelocity = body.position.moveTowards(this, velocity)
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