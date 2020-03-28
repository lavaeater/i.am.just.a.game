package ai.npc

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import data.Needs
import data.NeedsAndStuff
import data.Npc
import screens.Mgo
import systems.AiAndTimeSystem


abstract class NpcTask : LeafTask<Npc>() {
    fun timeHasPassed() {
        `object`.timeHasPassed(AiAndTimeSystem.minutesPerTick)
    }
}
/**
 * Completely disregard state machine's existince
 * for these tasks. We will consider the behavior tree
 * to be the state machine for the NPC
 */

abstract class NeedTask : NpcTask() {
    @TaskAttribute(required = true)
    var need : String = "Fun"
}



class FindRandomPlace {
    /*
    This is not necessary.

    The Npc will ALWAYS have a need - the need for fun.

    This need is satisfied by going to random places. We can skip that entire tree.
     */
}

class WalkingTo: NeedTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as WalkingTo
    }

    override fun execute(): Status {
        timeHasPassed()
        val npc = `object`

        return if(npc.onTheMove) Status.RUNNING else Status.SUCCEEDED
    }

}

class FindWhereToSatisfy: NeedTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        timeHasPassed()
        val npc = `object`

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
        timeHasPassed()
        val npc = `object`

        return if (npc.hasNeed(need)) {
            val activity = NeedsAndStuff.activities[NeedsAndStuff.needsToActivities[need]]!!
            npc.applyCosts(activity)
            Status.RUNNING
        } else {
            Status.SUCCEEDED
        }
    }
}

class HasNeed : NeedTask() {

    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        timeHasPassed()
        val npc = `object`

        //1. Get the need we're checking (this means we can have more complex behaviors if needed
        /*
        Why the property? It is there as a check that we actually have setup the need somewhere else
         */
        return if (npc.hasNeed(need))
            Status.SUCCEEDED
        else
            Status.FAILED
    }
}

class CanSatisfy: NeedTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {

        return task as CanSatisfy
    }

    override fun execute(): Status {
        timeHasPassed()
        val npc = `object`

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

