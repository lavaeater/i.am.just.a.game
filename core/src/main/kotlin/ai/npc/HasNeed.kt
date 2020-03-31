package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.NeedsAndStuff
import data.Npc

class HasNeed : NeedTask() {

    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        timeHasPassed()

        //1. Get the need we're checking (this means we can have more complex behaviors if needed
        /*
        Why the property? It is there as a check that we actually have setup the need somewhere else
         */
        return if (hasNeed(need))
            Status.SUCCEEDED
        else
            Status.FAILED
    }
}

class HasAnyNeed : NeedTask() {

    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        timeHasPassed()

        //1. Get the need we're checking (this means we can have more complex behaviors if needed
        /*
        Why the property? It is there as a check that we actually have setup the need somewhere else
         */
        return if (hasAnyNeed())
            Status.SUCCEEDED
        else
            Status.FAILED
    }
}

class WhatIsMyNeed : NeedTask() {

    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        timeHasPassed()

        for(need in NeedsAndStuff.prioritizedNeeds) {
            if(hasNeed(need)) {
                npc.currentNeed = need
                break
            }
        }
        
        //1. Get the need we're checking (this means we can have more complex behaviors if needed
        /*
        Why the property? It is there as a check that we actually have setup the need somewhere else
         */
        return if (hasNeed(need))
            Status.SUCCEEDED
        else
            Status.FAILED
    }
}