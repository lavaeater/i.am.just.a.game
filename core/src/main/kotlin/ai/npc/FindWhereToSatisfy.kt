package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.Npc

class FindWhereToSatisfy: NeedTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        timeHasPassed()

        val whereToSatisfy = (Satisfiers.whereToSatisfyResolvers[need] ?: error("No resolver found for need ${need}"))(npc)
        npc.walkTo(whereToSatisfy)
        return Status.SUCCEEDED
    }
}