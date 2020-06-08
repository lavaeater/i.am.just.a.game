package systems

import ai.npc.TravelMode
import atPlace
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.physics.box2d.Body
import components.Box2dBodyComponent
import components.NpcComponent
import data.Npc
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.toMutable
import ktx.math.vec2
import moveFromTo
import data.Place

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
    private lateinit var whereAreWeGoing: Pair<Place, TravelMode>

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val npc = npcMpr[entity].npc

        val body = bodyMpr[entity]!!.body
        if (npc.onTheMove) {

            //get the place to go to
            whereAreWeGoing = npc.thePlaceIWantToBe

            when (whereAreWeGoing.second) {
                TravelMode.Walking -> walk(npc, body)
                TravelMode.Zipping -> {
                    body.setTransform(npc.thePlaceIWantToBe.first.center.toMutable(), body.angle)
                    body.isAwake = true
                }
            }
          npc.currentPosition = body.position
          if (npc.atPlace(whereAreWeGoing.first))
            npc.stopDoingIt()
        }

        if (!npc.onTheMove)
            body.linearVelocity = vec2(0f, 0f)
    }

    private fun walk(npc: Npc, body: Body) {
        //Walking now means traversing a list of items.

        if(npc.currentPath.second.any() && npc.currentPath.second.first().data.dst2(npc.currentPosition.x, npc.currentPosition.y) < 2f) {
            npc.currentPath.second.removeAt(0) //Remove the first node
        }
        npc.currentPath.second.firstOrNull()?.data?.toMutable()?.moveFromTo(body, npc.speed * AiAndTimeSystem.secondsPerSecond)
    }
}
