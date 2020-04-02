package systems

import ai.npc.TravelMode
import atPlace
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import components.Box2dBodyComponent
import components.NpcComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.toMutable
import ktx.math.vec2
import moveFromTo
import screens.Place

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

    private var somekindOfSpeedFactor = 2f * AiAndTimeSystem.secondsPerSecond

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val npc = npcMpr[entity].npc

        val body = bodyMpr[entity]!!.body
        if (npc.onTheMove) {

            //Add modified social stuff later. - maybe the npc's need to arrange a meeting and so on?
//      if (npc.meetingAFriend) {
//        npc.friendToGoTo?.currentPosition?.moveFromTo(body, somekindOfSpeedFactor)
//        if (npc.circleOfConcern.contains(npc.friendToGoTo?.currentPosition))
//          npc.stopDoingIt()
//      } else {

            //get the place to go to
            whereAreWeGoing = npc.thePlaceIWantToBe

            when (whereAreWeGoing.second) {
                TravelMode.Walking -> whereAreWeGoing.first.center.toMutable().moveFromTo(body, somekindOfSpeedFactor)
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
}
