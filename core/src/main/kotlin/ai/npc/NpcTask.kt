package ai.npc

import com.badlogic.gdx.ai.btree.LeafTask
import data.*
import systems.AiAndTimeSystem
import java.lang.Math.abs

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

    fun hasAnyNeed() : Boolean {
        return npc.npcStats.statsMap.values.any { it in NeedsAndStuff.lowRange }
    }

    fun hasNeed(need:String) : Boolean {
        val stat = npc.npcStats.statsMap[need]
        return if(need == Needs.Rest && npc.iWillStayAtHome) true else stat in NeedsAndStuff.lowRange
    }

    private fun applyCosts() {
        val cost = NeedsAndStuff.getCostForActivity(npc.npcState)
        applyCosts(cost)
    }

    fun applyCosts(cost: Cost) {
        val timeFactor = 60 / AiAndTimeSystem.minutesPerTick.toInt()
        for((k, c) in cost.costMap) {
            val actualCost = c / timeFactor
            npc.npcStats.statsMap[k] = (npc.npcStats.statsMap[k]!! - actualCost).coerceIn(NeedsAndStuff.fullRange)
        }
    }
}