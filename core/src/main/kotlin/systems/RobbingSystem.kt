package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import components.Box2dBodyComponent
import components.NpcComponent
import components.RobbingComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.ashley.remove
import ktx.log.info
import ktx.math.amid

class RobbingSystem : IteratingSystem(allOf(NpcComponent::class, Box2dBodyComponent::class, RobbingComponent::class).get()) {

  private val bodyMapper = mapperFor<Box2dBodyComponent>()
  private val robComponentMapper = mapperFor<RobbingComponent>()

  override fun processEntity(entity: Entity, deltaTime: Float) {
    val robComponent = robComponentMapper[entity]
    val robberBody = bodyMapper[entity]
    val targetBody = bodyMapper[robComponent.targetEntity]
    if(robberBody.body.isWithin(3f, targetBody.body)) {
      tryToRob(robComponent)
    } else {
      robberBody.body.moveTowards(targetBody.body)
    }
  }

  private fun tryToRob(robbingComponent: RobbingComponent) {
    val robber = robbingComponent.robber
    val target =  robbingComponent.target

    if(target.money > 0) {
      val robAmount = (target.money / 2 amid target.money / 4).random()
      target.money-=robAmount
      robber.money+=robAmount
        info { "${robber.name} stole $robAmount from ${target.name}. Bad boy!" }
      robber.robberyWin()
    } else {
        info { "${robber.name} failed at robbery" }
      robber.robberyFailed()
    }
    robbingComponent.robberEntity.remove<RobbingComponent>()
  }
}