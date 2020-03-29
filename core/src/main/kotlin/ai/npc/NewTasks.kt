package ai.npc

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import data.*
import data.NeedsAndStuff.Companion.fullRange
import data.NeedsAndStuff.Companion.lowRange
import screens.Mgo


abstract class NpcTask : LeafTask<Npc>() {

    val npc : Npc get() = `object`

    fun timeHasPassed() {
        applyCosts()
    }

    fun checkIfNpcDied() {
        if(npc.npcStats.statsMap[Needs.Fuel]!! <= lowRange.first + 1) {
            npc.die()
        }
    }

    fun hasNeed(need:String) : Boolean {
        val stat = npc.npcStats.statsMap[need]
        return stat in lowRange
    }

    private fun applyCosts() {
        val cost = NeedsAndStuff.costs[npc.npcState] ?: error("No activity found")
        applyCosts(cost)
    }

    fun applyCosts(cost: Cost) {

        for((k, c) in cost.costMap) {
            npc.npcStats.statsMap[k] = (npc.npcStats.statsMap[k]!! - c).coerceIn(fullRange)
        }
    }
}
/**
 * Completely disregard state machine's existince
 * for these tasks. We will consider the behavior tree
 * to be the state machine for the NPC
 */

abstract class NeedTask : NpcTask() {
    @JvmField
    @TaskAttribute(required = true)
    var need : String = "Money"
}

class HasNeed : NeedTask() {

    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        timeHasPassed()

        //1. Get the need we're checking (this means we can have more complex behaviors if needed
        /*
        Why the property? It is there as a check that we actually have setup the need somewhere else
         */
        return if (hasNeed(need))
            Status.SUCCEEDED
        else
            Status.FAILED
    }
}


class FindRandomPlace {
    /*
    This is not necessary.

    The Npc will ALWAYS have a need - the need for fun.

    This need is satisfied by going to random places. We can skip that entire tree.
     */
}

class WalkingTo: NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as WalkingTo
    }

    override fun execute(): Status {

        applyCosts(NeedsAndStuff.costs[Activity.OnTheMove]?: error("Couldn't find cost for ${Activity.OnTheMove}"))

        return if(npc.onTheMove) Status.RUNNING else Status.SUCCEEDED
    }

}

class FindWhereToSatisfy: NeedTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        timeHasPassed()

        val whereToSatisfy = (Sf.whereToSatisfyResolvers[need] ?: error("No resolver found for need ${need}"))(npc)
        npc.walkTo(whereToSatisfy)
        return Status.SUCCEEDED
    }

}

class SatisfyNeed : NeedTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as SatisfyNeed
    }

    override fun execute(): Status {
        return if (hasNeed(need)) {
            val cost = NeedsAndStuff.costs[NeedsAndStuff.needsToActivities[need]]!!
            if(npc.npcState != cost.activity)
                npc.acceptEvent(NeedsAndStuff.activitiesToEvents[cost.activity]?: error("Event not found for $cost"))

            applyCosts(cost)
            Status.RUNNING
        } else {
            npc.stopDoingIt()
            Status.SUCCEEDED
        }
    }
}

class CanSatisfy: NeedTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {

        return task as CanSatisfy
    }

    override fun execute(): Status {
        timeHasPassed()

        val satisfier = Sf.satisfiableResolvers[need] ?: error("No satisifyResolver found for need ${need}")
        return if (satisfier(npc))
            Status.SUCCEEDED
        else
            Status.FAILED
    }
}

class Sf {

    companion object Sf {
        val satisfiableResolvers = mapOf(
                Needs.Fuel to { npc: Npc -> canIEatHere(npc) },
                Needs.Money to { npc: Npc -> isThisWhereIWork(npc) },
                Needs.Rest to { npc: Npc -> canISleepHere(npc) })

        val whereToSatisfyResolvers = mapOf(
                Needs.Rest to { npc: Npc -> npc.home },
                Needs.Money to  { npc: Npc -> npc.workPlace },
        Needs.Fuel to { npc: Npc -> Mgo.restaurants.random()}
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

