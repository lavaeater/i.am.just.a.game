package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.math.MathUtils
import components.NpcComponent
import components.PlayerComponent
import components.TransformComponent
import components.VisibleComponent
import data.Player
import injection.Ctx
import map.isInRange
import ktx.ashley.allOf
import ktx.ashley.has
import ktx.ashley.mapperFor
import ktx.ashley.remove

class PlayerEntityDiscoverySystem() :
    IntervalIteratingSystem(allOf(TransformComponent::class, NpcComponent::class).get(),0.25f,1) {

  val transMpr = mapperFor<TransformComponent>()
  val npcMpr = mapperFor<NpcComponent>()
  val visibilityMapper = mapperFor<VisibleComponent>()

  val player by lazy { Ctx.context.inject<Player>()}
  override fun processEntity(entity: Entity) {

    val playerPos = Pair(player.currentX, player.currentY)
    val npcPos = transMpr[entity].position.toTile()

    if(npcPos.isInRange(playerPos, player.sightRange)) {
      if(!entity.has(visibilityMapper)) {
        val playerSkill = player.skills["tracking"]!!
        val npc = npcMpr[entity].npc
        val npcSkill = npc.skills["stealth"]!!

        //How do we do discovery roll? Player skill - enemy counter skill, if under => success
        if (skillRoll(playerSkill, npcSkill)) {
          //The player sees the npc, it should now be rendered!
          entity.add(VisibleComponent())
        }
      }
    } else {
      //Entities that aren't in visibility range are invisible to the player!
      if(entity.has(visibilityMapper)) {
        entity.remove<VisibleComponent>()
      }
    }

  }

  private fun skillRoll(baseSkill: Int, skillModifier: Int): Boolean {
    return MathUtils.random(99) + 1 < baseSkill - skillModifier
  }

}

