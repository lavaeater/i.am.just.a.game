package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.Npc

class CanSatisfy: NeedTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {

        return task as CanSatisfy
    }

    override fun execute(): Status {
        timeHasPassed()

        val satisfier = Satisfiers.satisfiableResolvers[need] ?: error("No satisifyResolver found for need ${need}")
        return if (satisfier(npc))
            Status.SUCCEEDED
        else
            Status.FAILED
    }
}