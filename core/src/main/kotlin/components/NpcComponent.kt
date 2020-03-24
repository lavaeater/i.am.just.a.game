package components

import com.badlogic.ashley.core.Component

class Npc(val name: String, val id: String) {
}

class NpcComponent(val npc: Npc): Component