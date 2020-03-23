package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.math.MathUtils
import components.*
import data.Player
import injection.Ctx
import ktx.ashley.allOf
import ktx.ashley.has
import ktx.ashley.mapperFor
import ktx.ashley.remove
import map.isInRange

class FeatureDiscoverySystem() :
    IntervalIteratingSystem(allOf(TransformComponent::class, FeatureComponent::class).get(),1f,1) {

  val transMpr = mapperFor<TransformComponent>()
  val featureMapper = mapperFor<FeatureComponent>()
  val visibilityMapper = mapperFor<VisibleComponent>()

  val player by lazy { Ctx.context.inject<Player>()}

  override fun processEntity(entity: Entity) {

    val playerPos = Pair(player.currentX, player.currentY)
    val featurePos = transMpr[entity].position.toTile()

    if(featurePos.isInRange(playerPos, player.sightRange)) {
      if (!entity.has(visibilityMapper)) {
        val playerSkill = player.skills["tracking"]!!
        val featureSkill = featureMapper[entity].place.stealth
        //How do we do discovery roll? Player skill - enemy counter skill, if under => success
        if (skillRoll(playerSkill, featureSkill)) {
          //The player sees the npc, it should now be rendered!
          entity.add(VisibleComponent())
        }
      }
    }

    //Features that have once been discovered should be rendered in the fog of war, yay!
//    } else {
//      //Entities that aren't in visibility range are invisible to the player!
//      if(entity.has(visibilityMapper)) {
//        entity.remove<VisibleComponent>()
//      }
    }

  private fun skillRoll(baseSkill: Int, skillModifier: Int): Boolean {
    return MathUtils.random(99) + 1 < baseSkill - skillModifier
  }
}