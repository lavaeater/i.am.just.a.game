package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.Activity
import data.NeedsAndStuff
import data.Npc
import screens.Mgo

class WalkToNearestTravelHub() : NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as WalkToNearestTravelHub
    }

    override fun execute(): Status {
        applyCosts(NeedsAndStuff.getCostForActivity(Activity.OnTheMove))

        //Find nearest travelhub
        val hub = Mgo.travelHubs.findPlaceNearby(npc.placesToGoTo.first())
        //Thats the hub to TRAVEL to, not the hub to WALK to. We need hub IDs

        /*
        And we don't need walking to, travelling to etc.

        They can now be the same.

        The npc will now find a path instead. And the cueue will contain
        pairs of modes + places. yay.

         */
        npc.addPlaceToGoTo(hub)
        npc.travelToFirstPlace()
        return Status.SUCCEEDED
    }
}