package ai.npc

import com.badlogic.gdx.ai.btree.LeafTask
import data.*
import systems.AiAndTimeSystem
import java.lang.Math.abs

abstract class NpcTask : LeafTask<Npc>() {

    val walkRadius = 100f
    val npc : Npc get() = `object`

    fun timeHasPassed() {
        applyCosts()
    }

    fun checkIfNpcDied() {
        if(npc.npcStats.statsMap[Needs.Fuel]!! <= Needs.lowRange.first + 1) {
            npc.die()
        }
    }

    fun hasAnyNeed() : Boolean {
        return npc.npcStats.statsMap.values.any { it in Needs.lowRange }
    }

    /**
     * Returns true if the need in question is in the low Range of stats
     */
    fun hasNeed(need:String) : Boolean {
        val stat = npc.npcStats.statsMap[need]
        return if(need == Needs.Rest && npc.iWillStayAtHome) true else stat in Needs.lowRange
    }

    /**
     * This makes npc:s stay at work and restaurants longer, maybe,
     * making their days more constant.
     */
    fun stillHasNeed(need:String) : Boolean {
        val stat = npc.npcStats.statsMap[need]
        return if(need == Needs.Rest && npc.iWillStayAtHome) true else stat in Needs.lowNormal
    }

    private fun applyCosts() {
        val cost = Needs.getCostForActivity(npc.npcState)
        applyCosts(cost)
    }

    fun applyCosts(cost: Cost) {
        val timeFactor = 60 / AiAndTimeSystem.minutesPerTick.toInt()
        for((k, c) in cost.costMap) {
            val actualCost = c / timeFactor
            npc.npcStats.statsMap[k] = (npc.npcStats.statsMap[k]!! - actualCost).coerceIn(Needs.fullRange)
        }
    }
}