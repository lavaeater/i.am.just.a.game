package ai.npc

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.math.Rectangle
import data.Needs
import data.Npc
import data.NpcDataAndStuff
import ktx.math.random
import screens.Mgo
import screens.Place
import screens.PlaceType




class FindRandomPlace {
    /*
    This is not necessary.

    The Npc will ALWAYS have a need - the need for fun.

    This need is satisfied by going to random places. We can skip that entire tree.
     */
}

class WalkTo: LeafTask<Npc>() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as WalkTo
    }

    override fun execute(): Status {
        val npc = `object`

        return if(npc.onTheMove)
            Status.RUNNING
        else
            Status.SUCCEEDED //This means we weren't going anywhere I suppose
    }

}

class FindWhereToSatisfy: LeafTask<Npc>() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        val npc = `object`

        if(npc.hasAnyNeeds()) {
            val topNeed = npc.getTopNeed()
            val whereToSatisfy = (Sf.whereToSatisfyResolvers[topNeed] ?: error("No resolver found"))(npc)
            npc.walkTo(whereToSatisfy)
            return Status.SUCCEEDED
        }

        return Status.FAILED //What does this mean?
    }

}

class SatisfyNeed : LeafTask<Npc>() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as SatisfyNeed
    }

    var currentTopNeed: Needs? = null

    override fun execute(): Status {
        val npc = `object`

        if (currentTopNeed != null) {

            return if (npc.hasNeed(currentTopNeed!!)) {
                val activity = NpcDataAndStuff.activities[NpcDataAndStuff.needsToActivities[currentTopNeed!!]]!!
                npc.applyCosts(activity)
                Status.RUNNING
            } else {
                currentTopNeed = null
                Status.SUCCEEDED
            }
        } else {
            return if (npc.hasAnyNeeds()) {
                currentTopNeed = npc.getTopNeed()
                val activity = NpcDataAndStuff.activities[NpcDataAndStuff.needsToActivities[currentTopNeed!!]]!!
                npc.applyCosts(activity)
                Status.RUNNING
            } else {
                Status.SUCCEEDED
            }
        }
    }
}

/**
 * Completely disregard state machine's existince
 * for these tasks. We will consider the behavior tree
 * to be the state machine for the NPC
 */
class HasNeed : LeafTask<Npc>() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        val npc = `object`
        return if (npc.hasAnyNeeds())
            Status.SUCCEEDED
        else
            Status.FAILED
    }
}

class CanSatisfy: LeafTask<Npc>()  {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {

        return task as CanSatisfy
    }

    override fun execute(): Status {
        val npc = `object`

        if(npc.hasAnyNeeds()) {
            val topNeed = npc.getTopNeed()
            val satisfier = Sf.satisfiableResolvers[topNeed] ?: error("No satisifyResolver found for need")
            return if(satisfier(npc))
                Status.SUCCEEDED
            else
                Status.FAILED
        }
        return Status.FAILED //How do we even handle this? This should never ever happen, actually
    }


}

class Sf {

    companion object Sf {
        val satisfiableResolvers = mapOf(
                Needs.Fun to { npc: Npc -> (0..1).random() == 1 },
                Needs.Fuel to { npc: Npc -> canIEatHere(npc) },
                Needs.Money to { npc: Npc -> isThisWhereIWork(npc) },
                Needs.Rest to { npc: Npc -> canISleepHere(npc) })

        val whereToSatisfyResolvers = mapOf(
                Needs.Rest to { npc: Npc -> npc.home },
                Needs.Money to  { npc: Npc -> npc.workPlace },
        Needs.Fuel to { npc: Npc -> Mgo.restaurants.random()},
        Needs.Fun to { npc: Npc -> Place("random made up funny house", PlaceType.Tivoli, box = Rectangle(Mgo.workPlaceRange.random(),Mgo.workPlaceRange.random(), 10f, 10f)) }
        )

        fun isThisWhereIWork(npc: Npc) :Boolean {
            return npc.workPlace.box.contains(npc.currentPosition)
        }

        fun canIEatHere(npc: Npc) : Boolean {
            return Mgo.restaurants.any { it.box.contains(npc.currentPosition) }
        }

        fun canISleepHere(npc: Npc) : Boolean {
            return npc.home.box.contains(npc.currentPosition)
        }
    }
}

