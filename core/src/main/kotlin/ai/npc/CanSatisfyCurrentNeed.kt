package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.Npc

class CanSatisfyCurrentNeed: NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {

        return task as CanSatisfyCurrentNeed
    }

    override fun execute(): Status {
        timeHasPassed()

        val canSatisfy = Satisfiers.satisfiableResolvers[npc.currentNeed] ?: error("No satisifyResolver found for need ${npc.currentNeed}")
        return if (canSatisfy(npc))
            Status.SUCCEEDED
        else
            Status.FAILED
    }
}