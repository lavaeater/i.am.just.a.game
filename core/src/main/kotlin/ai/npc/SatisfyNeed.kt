package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.NeedsAndStuff
import data.Npc

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