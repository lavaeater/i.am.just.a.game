package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.NeedsAndStuff
import data.Npc

class SatisfyNeed : NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as SatisfyNeed
    }

    override fun execute(): Status {
        return if (hasNeed(npc.currentNeed)) {
            val cost = NeedsAndStuff.getCostForNeed(npc.currentNeed)
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