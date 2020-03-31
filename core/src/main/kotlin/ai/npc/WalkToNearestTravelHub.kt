package ai.npc

import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import data.Activity
import data.NeedsAndStuff
import data.Npc
import ktx.math.vec2
import screens.Mgo
import screens.PlaceType

class WalkToNearestTravelHub() : NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as WalkToNearestTravelHub
    }

    @JvmField
    @TaskAttribute(required = true)
    var hubType : String? = null

    override fun execute(): Status {
        applyCosts(NeedsAndStuff.getCostForActivity(Activity.OnTheMove))

        //Find nearest non-center travelHub
        val hub = Mgo.travelHubs.filter { (it.type as PlaceType.TravelHub).hubType == hubType!! }.minBy { vec2(it.box.x, it.box.y).dst(npc.currentPosition) }?: error("No travel hub found, tweak distances")

        npc.walkTo(hub)
        return Status.SUCCEEDED
    }
}

