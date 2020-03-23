package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import data.Npc
import data.NpcState
import components.Box2dBodyComponent
import components.NpcComponent
import map.tileWorldCenter
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.*

class NpcControlSystem : IteratingSystem(allOf(
    NpcComponent::class,
    Box2dBodyComponent::class).get(),10) {

  val npcMpr = mapperFor<NpcComponent>()
  val bodyMpr = mapperFor<Box2dBodyComponent>()

  override fun processEntity(entity: Entity, deltaTime:Float) {
    val npc = npcMpr[entity].npc
    val body = bodyMpr[entity]!!.body
    when(npc.state) {
      NpcState.Idle -> return
      NpcState.Wandering -> comeWalkWithMe(npc, body)
      NpcState.WalkingTo -> if(npc.tileFound) walkToTile(npc.foundX, npc.foundY, body)
      NpcState.Scavenging -> return //Replace with some animation or some other stuff
      NpcState.Searching -> return //This code doesn't need to do anything for this state, maybe anim later?
    }

    npc.apply {
      currentX = body.position.tileX()
      currentY = body.position.tileY()
    }
  }

  private fun walkToTile(x:Int, y:Int, body: Body) {
      moveFromTo(Pair(x,y).tileWorldCenter(),body)
  }

  private fun comeWalkWithMe(npc: Npc, body: Body) {
    //The Npc manages its own state, preferrably?
    moveFromTo(Pair(npc.foundX, npc.foundY).tileWorldCenter(), body)
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