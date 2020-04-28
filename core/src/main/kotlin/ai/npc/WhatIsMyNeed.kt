package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.Needs
import data.Npc

class WhatIsMyNeed : NpcTask() {

    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        timeHasPassed()

        for(need in Needs.prioritizedNeeds) {
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