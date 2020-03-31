package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.Activity
import data.NeedsAndStuff
import data.Npc
import screens.Mgo

class CanTravel : NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as CanTravel
    }
    override fun execute(): Status {
        applyCosts(NeedsAndStuff.getCostForActivity(Activity.OnTheMove))

        return if(Mgo.travelHubs.any { it.box.contains(npc.currentPosition) })
            Status.SUCCEEDED
        else
            Status.FAILED
    }
}