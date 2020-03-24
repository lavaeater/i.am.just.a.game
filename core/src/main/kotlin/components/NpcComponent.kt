package components

import com.badlogic.ashley.core.Component

class Npc(val name: String, val id: String) {
    fun scavenge(): Boolean {
        return true
    }

    fun lostInterest() {
        TODO("Not yet implemented")
    }

    fun wander(): Boolean {
        return true
    }

    fun walkTo(): Boolean {
        return true
    }
}

class NpcComponent(val npc: Npc): Component