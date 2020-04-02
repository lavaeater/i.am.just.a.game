package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.Npc


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
        npc.addPlaceToGoTo(placeToSatisfyNeed, TravelMode.Walking)

        /*
        We found a place, success! Can we fail? Naah.
         */
        return Status.SUCCEEDED
    }
}


