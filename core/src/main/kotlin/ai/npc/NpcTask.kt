package ai.npc

import com.badlogic.gdx.ai.btree.LeafTask
import data.Cost
import data.Needs
import data.NeedsAndStuff
import data.Npc

abstract class NpcTask : LeafTask<Npc>() {

    val npc : Npc get() = `object`

    fun timeHasPassed() {
        applyCosts()
    }

    fun checkIfNpcDied() {
        if(npc.npcStats.statsMap[Needs.Fuel]!! <= NeedsAndStuff.lowRange.first + 1) {
            npc.die()
        }
    }

    fun hasNeed(need:String) : Boolean {
        val stat = npc.npcStats.statsMap[need]
        return stat in NeedsAndStuff.lowRange
    }

    private fun applyCosts() {
        val cost = NeedsAndStuff.costs[npc.npcState] ?: error("No activity found")
        applyCosts(cost)
    }

    fun applyCosts(cost: Cost) {

        for((k, c) in cost.costMap) {
            npc.npcStats.statsMap[k] = (npc.npcStats.statsMap[k]!! - c).coerceIn(NeedsAndStuff.fullRange)
        }
    }
}