package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.Activity
import data.NeedsAndStuff
import data.Npc

class WalkTo: NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as WalkTo
    }

    override fun execute(): Status {

        applyCosts(NeedsAndStuff.getCostForActivity(Activity.OnTheMove))
        val whereToSatisfy = (Satisfiers.whereToSatisfyResolvers[npc.currentNeed]
                ?: error("No resolver found for need ${npc.currentNeed}"))(npc)
        npc.walkTo(whereToSatisfy.random())
        return Status.SUCCEEDED
    }

}

