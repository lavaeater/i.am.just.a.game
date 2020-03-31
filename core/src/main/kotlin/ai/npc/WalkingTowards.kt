package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.Activity
import data.NeedsAndStuff
import data.Npc

class WalkingTowards: NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as WalkTo
    }

    override fun execute(): Status {
        /*
        We are always on the move in this node, the tree makes decisions, not anything else
         */

        applyCosts(NeedsAndStuff.getCostForActivity(Activity.OnTheMove))
        return if (npc.thePlaceIWantToBe.box.contains(npc.currentPosition)) Status.SUCCEEDED else Status.RUNNING
    }
}