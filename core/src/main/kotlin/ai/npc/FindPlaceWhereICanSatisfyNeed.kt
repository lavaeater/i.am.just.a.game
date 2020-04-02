package ai.npc

import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.math.Vector2
import data.Activity
import data.NeedsAndStuff
import data.Npc
import ktx.math.vec2
import screens.Mgo
import screens.Place


class FindPlaceWhereICanSatisfyNeed: NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as FindPlaceWhereICanSatisfyNeed
    }
    override fun execute(): Status {
        timeHasPassed()

        /*
        The below function call should return ONE place - this place will be the place we want to go to, for now. It
        will be added to a queue of places to go to.

        Every type of place can have its own way of determining how to do that.
         */
        val placeToSatisfyNeed = (Satisfiers.whereToSatisfyResolvers[npc.currentNeed] ?: error("No resolver found for need ${npc.currentNeed}"))(npc)

        /*
        We assume walking for now, is normal.
         */
        npc.addPlaceToGoTo(placeToSatisfyNeed, TMode.Walking)

        /*
        We found a place, success! Can we fail? Naah.
         */
        return Status.SUCCEEDED
    }
}

class TravelByTraversing: NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as TravelByTraversing
    }
    override fun execute(): Status {

        applyCosts(NeedsAndStuff.getCostForActivity(Activity.OnTheMove))

        /*
        The complicated bit here is the cost application
        and the timing of the AI System

        The AI isn't thinking or doing things that often, which is fine.

        But something has to control the npc's movement,
        and that is the npc control system.

        So, this system (the AI system) should be active on certain parts of the
        npcs values and stats, and the npc control system on others. So

        the AI system should say stuff like "walk to", "zip to" and the NPC control,
        which isn't a control system as much as a "where is this physics body going
        and how"-system, should make sure it graphically and technically happens.

        This system is totally fine with the npc NOT being in some certain state
        etc, it will check the position to determine the next ACTION, not some state.

        Again, states are for controlling sprites etc, I would assume, perhaps, maybe.

        So, we should have a place we want to be. If we are walking to it,
        the Npc travel mode should be Walking. Use WalkTo
         */

        





        return Status.SUCCEEDED
    }
}

class FindAPathToAPlace: NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as FindAPathToAPlace
    }
    override fun execute(): Status {
        timeHasPassed()

        /*
        The below function call should return ONE place - this place will be the place we want to go to, for now. It
        will be added to a queue of places to go to.

        Every type of place can have its own way of determining how to do that.



         */

        /*
        The path to a place can simply be - walking. If we cannot reach the first place in the queue by walking, we will
        iterate through the process of creating a path!
         */

        val targetPlace = npc.placesToGoTo.first()
        return when {
            targetPlace.second != TMode.Walking
                /*
                The place we want is a place to zip to, so we're done.
                 */
                -> Status.SUCCEEDED
            npc.placeInWalkingRange(targetPlace.first) -> {
                /*
            We can walk there, no need to do anything else here...
             */
                Status.SUCCEEDED
            }
            else -> {
                //This is where we will zip
                val hub = Mgo.travelHubs.findPlaceNearby(targetPlace.first)

                npc.addPlaceToGoTo(hub, TMode.Zipping)

                //By going to the nearest hub!

                val hubToWalkTo = Mgo.travelHubs.findNearest(npc.currentPosition.x, npc.currentPosition.y)

                npc.addPlaceToGoTo(hubToWalkTo, TMode.Walking)
                /*
            We found a place, success!
             */
                Status.SUCCEEDED
            }
        }
    }
}



enum class TMode {
    Walking,
    Zipping
}

/**
 * Takes a place and finds the travel hub (subway station)
 * closeset to it. This will be the target hub for the npc
 */
fun List<Place>.findPlaceNearby(place: Place) : Place {
    val hub = this.findNearest(place.center.x, place.center.y)
    return hub
}

fun List<Place>.findNearest(x: Float, y:Float): Place {
    val place = this.minBy { it.center.dst(x, y) }
    return place!!
}


fun Npc.placeInWalkingRange(place: Place) :Boolean {
    return this.currentPosition.dst(place.center.x, place.center.y) < this.walkingRange
}

fun Npc.atPlace(place: Place) :Boolean {
    return place.box.contains(this.currentPosition)
}

fun Vector2.placesInRange(range: Float, places: List<Place>) : List<Place> {
    return places.filter { vec2(it.box.x + it.box.width / 2, it.box.y +it.box.height /2).dst(this) < range }
}