package ai.npc

import com.badlogic.gdx.ai.btree.Task
import data.Npc

class FindAFriend: NpcTask() {
    override fun copyTo(task: Task<Npc>?): Task<Npc> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        timeHasPassed()

        val friendToMeet = npc.friends.minBy { it.currentPosition.dst(npc.currentPosition) }?: error("Couldn't find any friends at all - maybe they are all dead?")
        npc.goToMeet(friendToMeet)
        return Status.SUCCEEDED
    }
}