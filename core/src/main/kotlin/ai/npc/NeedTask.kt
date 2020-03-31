package ai.npc

import com.badlogic.gdx.ai.btree.annotation.TaskAttribute

/**
 * Completely disregard state machine's existince
 * for these tasks. We will consider the behavior tree
 * to be the state machine for the NPC
 */

abstract class NeedTask : NpcTask() {
    var need : String = "Money" //Will instead be set by the task "WhatIsMyNeed" - and we will have a default need of like "Nothing, but in this case, money!
}