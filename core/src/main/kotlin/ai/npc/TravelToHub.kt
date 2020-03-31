package ai.npc

import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import data.Activity
import data.NeedsAndStuff
import data.Npc
import screens.Mgo
import screens.PlaceType

class TravelToHub() : NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as WalkToNearestTravelHub
    }

    @JvmField
    @TaskAttribute(required = true)
    var hubType : String? = null //HubTypes are basically names - if unique, they represent a specific place, if general, just random.

    override fun execute(): Status {
        applyCosts(NeedsAndStuff.getCostForActivity(Activity.OnTheMove))

        //Find nearest non-center travelHub
        val hub = Mgo.travelHubs.first { (it.type as PlaceType.TravelHub).hubType == hubType!! }

        npc.zipTo(hub)
        return Status.SUCCEEDED
    }
}

