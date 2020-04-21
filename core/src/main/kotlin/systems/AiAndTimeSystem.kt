package systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.math.Circle
import components.AiComponent
import components.NpcComponent
import data.CoronaStats
import data.CoronaStatus
import data.Npc
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.log.info
import ktx.math.random
import java.time.LocalDateTime
import java.time.Period

class AiAndTimeSystem(startTime: Int = 6, minutes: Long = 5, interval: Float = 5f) : IntervalIteratingSystem(allOf(AiComponent::class).get(), interval, 5) {
  private val aiMapper = mapperFor<AiComponent<Npc>>()

  init {
    currentDateTime = LocalDateTime.of(2020, 1,1, startTime,0)
    minutesPerTick = minutes
  }

  override fun processEntity(entity: Entity) {
    aiMapper[entity].behaviorTree.step()
  }

  override fun updateInterval() {

    currentDateTime =  currentDateTime.plusMinutes(minutesPerTick)
    super.updateInterval()
  }



  companion object {
    var currentDateTime: LocalDateTime = LocalDateTime.of(2020, 1,1, 1,0)
    var minutesPerTick :Long = 15
    var interval = 1f
    val secondsPerSecond get() = minutesPerTick * 60 / interval
  }
}

