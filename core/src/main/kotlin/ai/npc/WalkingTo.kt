package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.Activity
import data.NeedsAndStuff
import data.Npc

class WalkingTo: NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as WalkingTo
    }

    override fun execute(): Status {

        applyCosts(NeedsAndStuff.costs[Activity.OnTheMove]?: error("Couldn't find cost for ${Activity.OnTheMove}"))

        return if(npc.onTheMove) Status.RUNNING else Status.SUCCEEDED
    }

}