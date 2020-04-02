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

        /*
        This makes more sense now - deque the first place in the places queue and
        go there!
         */

        npc.travelToFirstPlace()
        return Status.SUCCEEDED
    }
}

