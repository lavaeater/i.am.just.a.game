package components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import data.Npc

class RobbingComponent(val robber: Npc, val robberEntity: Entity, val target: Npc, val targetEntity: Entity): Component
