package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.CoronaStatus
import data.Needs
import data.NeedsAndStuff
import data.Npc

class WhatIsMyNeed : NpcTask() {

    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        timeHasPassed()

        if(npc.coronaStatus == CoronaStatus.Infected && npc.symptomatic) {
            npc.currentNeed = Needs.Rest
            return Status.SUCCEEDED
        }
        for(need in NeedsAndStuff.prioritizedNeeds) {
            if(hasNeed(need)) {
                npc.currentNeed = need
                return Status.SUCCEEDED
            }
        }
        //We could not ascertain what need the npc has, so set it to NOTHING
        npc.currentNeed = Needs.Nothing
        return Status.FAILED
    }
}