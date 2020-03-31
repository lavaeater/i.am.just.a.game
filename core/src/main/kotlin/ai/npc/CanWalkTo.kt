package ai.npc

import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.math.Vector2
import data.Npc
import ktx.math.vec2
import screens.Place


class CanWalkTo: NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        return task as CanWalkTo
    }
    override fun execute(): Status {
        timeHasPassed()

        val whereToSatisfy = (Satisfiers.whereToSatisfyResolvers[npc.currentNeed] ?: error("No resolver found for need ${npc.currentNeed}"))(npc)

        return if(npc.currentPosition.placesInRange(walkRadius, whereToSatisfy).any())
            Status.SUCCEEDED
        else
            Status.FAILED
    }
}

fun Vector2.placesInRange(range: Float, places: List<Place>) : List<Place> {
    return places.filter { vec2(it.box.x + it.box.width / 2, it.box.y +it.box.height /2).dst(this) < range }
}