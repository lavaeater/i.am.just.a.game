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

    override fun execute(): Status {
        applyCosts(NeedsAndStuff.getCostForActivity(Activity.OnTheMove))



        //We ARE at a hub. What type is it?

        //Find nearest non-center travelHub
        when((Mgo.travelHubs.first { it.box.contains(npc.currentPosition)}.type as PlaceType.TravelHub).hubType) {
            PlaceType.Suburban -> npc.zipTo(Mgo.travelHubs.first { (it.type as PlaceType.TravelHub).hubType == PlaceType.Central!! })
            PlaceType.Central -> npc.zipTo(Mgo.travelHubs.filter { (it.type as PlaceType.TravelHub).hubType == PlaceType.Suburban!! }.minBy {  })
        }

        return Status.SUCCEEDED
    }
}

