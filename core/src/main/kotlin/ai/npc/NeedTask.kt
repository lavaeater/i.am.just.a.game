package ai.npc

import com.badlogic.gdx.ai.btree.annotation.TaskAttribute

/**
 * Completely disregard state machine's existince
 * for these tasks. We will consider the behavior tree
 * to be the state machine for the NPC
 */

abstract class NeedTask : NpcTask() {
    @JvmField
    @TaskAttribute(required = true)
    var need : String = "Money"
}