package ai.npc

import atPlace
import com.badlogic.gdx.ai.btree.Task
import data.Activities
import data.Needs
import data.Npc

class TravelByTraversing: NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as TravelByTraversing
    }

    override fun execute(): Status {

        applyCosts(Needs.getCostForActivity(Activities.OnTheMove))

        /*
        we get here every tick

        If we use pathfinding and node-based travel,
        then the npc can simply have a path to work on and when that path is done,
        it just returns true, I guess?

        This is for walking destinations, only

        The npc himself will now completely know where he is going. How do we
        handle that in the NPC Control System? That system

        should only control movement. So, let's do this, but how?

        All destinations are now NODES, so no need to check boxes and stuff anymore,
        or maybe we could, of course... here's how it goes... this
        stuff controls properties on the npc so that the control
        system can move the character around and stuff.

        Since places have nodes, the paths are really easy to find - but it is also
        easy to teleport folks to the node they need to be at.

         */


        return if (!npc.placesToGoTo.any()) {
            Status.SUCCEEDED
        } else if(npc.atPlace(npc.placesToGoTo.first().first)) {
            //Then remove that place from the queue
            npc.placesToGoTo.removeFirst()
            Status.RUNNING
        } else {
            npc.travelToFirstPlace()
            Status.RUNNING
        }


    }
}