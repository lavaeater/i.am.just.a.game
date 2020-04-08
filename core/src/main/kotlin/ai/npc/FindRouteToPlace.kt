package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.Npc
import findNearest
import findPlaceNearby
import placeInWalkingRange
import screens.Mgo

class FindRouteToPlace: NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as FindRouteToPlace
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
            targetPlace.second != TravelMode.Walking
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

                npc.addPlaceToGoTo(hub, TravelMode.Zipping)

                //By going to the nearest hub!

                val hubToWalkTo = Mgo.travelHubs.findNearest(npc.currentPosition.x, npc.currentPosition.y)

                npc.addPlaceToGoTo(hubToWalkTo, TravelMode.Walking)
                /*
            We found a place, success!
             */
                Status.SUCCEEDED
            }
        }
    }
}