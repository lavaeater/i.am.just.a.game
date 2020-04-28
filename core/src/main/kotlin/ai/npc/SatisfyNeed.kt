package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.Needs
import data.Npc

class SatisfyNeed : NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as SatisfyNeed
    }

    override fun execute(): Status {
        return if (stillHasNeed(npc.currentNeed)) {
            val cost = Needs.getCostForNeed(npc.currentNeed)
            if(npc.npcState != cost.activity)
                npc.acceptEvent(Needs.activitiesToEvents[cost.activity]?: error("Event not found for $cost"))

            applyCosts(cost)
            Status.RUNNING
        } else {
            npc.stopDoingIt()
            Status.SUCCEEDED
        }
    }
}