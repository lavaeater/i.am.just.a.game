package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.ashley.systems.IntervalSystem
import components.AgentComponent
import components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor

class WorldFactsSystem : IntervalIteratingSystem(
    allOf(
        TransformComponent::class,
        AgentComponent::class).get(),0.5f) {
  private val transMpr = mapperFor<TransformComponent>()
  private val agentMpr = mapperFor<AgentComponent>()

  override fun processEntity(entity: Entity) {
    agentMpr[entity].agent.apply {
      currentX = transMpr[entity].position.tileX()
      currentY = transMpr[entity].position.tileY()
    }
  }
}

class GlobalWorldFactSystem : IntervalSystem(5f) {

  override fun updateInterval() {

  }

}