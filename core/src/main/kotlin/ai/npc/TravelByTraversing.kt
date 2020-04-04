package ai.npc

import atPlace
import com.badlogic.gdx.ai.btree.Task
import data.Activity
import data.NeedsAndStuff
import data.Npc

class TravelByTraversing: NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as TravelByTraversing
    }

    override fun execute(): Status {

        applyCosts(NeedsAndStuff.getCostForActivity(Activity.OnTheMove))

        /*
        The complicated bit here is the cost application
        and the timing of the AI System

        The AI isn't thinking or doing things that often, which is fine.

        But something has to control the npc's movement,
        and that is the npc control system.

        So, this system (the AI system) should be active on certain parts of the
        npcs values and stats, and the npc control system on others. So

        the AI system should say stuff like "walk to", "zip to" and the NPC control,
        which isn't a control system as much as a "where is this physics body going
        and how"-system, should make sure it graphically and technically happens.

        This system is totally fine with the npc NOT being in some certain state
        etc, it will check the position to determine the next ACTION, not some state.

        Again, states are for controlling sprites etc, I would assume, perhaps, maybe.

        So, we should have a place we want to be. If we are walking to it,
        the Npc travel mode should be Walking. Use WalkTo
         */

        // Do not remove places until we are at the place.
        //1. Are we at the place we want to go to?

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