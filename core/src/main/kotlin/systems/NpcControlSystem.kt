package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import components.Box2dBodyComponent
import components.NpcComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.log.info
import ktx.math.*

class NpcControlSystem : IteratingSystem(allOf(
    NpcComponent::class,
    Box2dBodyComponent::class).get(),10) {

  val npcMpr = mapperFor<NpcComponent>()
  val bodyMpr = mapperFor<Box2dBodyComponent>()

  override fun processEntity(entity: Entity, deltaTime:Float) {
    val npc = npcMpr[entity].npc
    val body = bodyMpr[entity]!!.body
    if(npc.onMyWay) {
      val whereToGo = body.position + npc.thisIsWhereIWantToBe
//      info { "${npc.name} is going to $whereToGo" }

      moveFromTo(whereToGo, body)

    if(whereToGo.dst(body.position) < 3f )
      npc.iAmThereNow()
    }

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