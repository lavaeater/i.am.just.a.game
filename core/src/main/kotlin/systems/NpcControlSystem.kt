package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import components.Box2dBodyComponent
import components.NpcComponent
import components.RobbingComponent
import components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.log.info
import ktx.math.*
import screens.MasterGameObjectWithStuff

class NpcControlSystem : IteratingSystem(allOf(
    NpcComponent::class,
    Box2dBodyComponent::class).get(),10) {

  val npcMpr = mapperFor<NpcComponent>()
  val bodyMpr = mapperFor<Box2dBodyComponent>()
  val robMapper = mapperFor<RobbingComponent>()

  override fun processEntity(entity: Entity, deltaTime:Float) {
    val npc = npcMpr[entity].npc
    val body = bodyMpr[entity]!!.body
    if(npc.onMyWay) {
      npc.thisIsWhereIWantToBe.moveFromTo(body)

    if(npc.thisIsWhereIWantToBe.dst(body.position) < 3f )
      npc.iAmThereNow()
    }

    if(npc.robbing && !robMapper.has(entity)) {
      val notBeingRobbedRightNow = entities.filter { !robMapper.has(it) }
      if(notBeingRobbedRightNow.any()) {
        val entityToRob = notBeingRobbedRightNow.random()
      } else {
        npc.couldNotRob()
      }

      info { "${npc.name} will now try to rob ${npcToRob.name}" }
    }
  }
}

class RobbingSystem : IteratingSystem(allOf(NpcComponent::class, TransformComponent::class, RobbingComponent::class).get()) {
  override fun processEntity(entity: Entity?, deltaTime: Float) {
  }

}

fun Vector2.moveFromTo(body:Body) {
  body.linearVelocity = body.position.moveTowards(this, 5f)
}

fun Vector2.directionalVelocity(velocity : Float) : Vector2 {
  return (vec2(0f,0f) - this).nor() * velocity
}

fun Vector2.moveTowards(target: Vector2, velocity: Float) : Vector2 {
  return (target - this).nor() * velocity
}