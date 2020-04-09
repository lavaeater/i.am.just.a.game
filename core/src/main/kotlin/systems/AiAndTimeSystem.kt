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

class Infectionator(private val engine: Engine) {
  private var needsInit = true
  private val npcMapper = mapperFor<NpcComponent>()
  private var npcs = listOf<Npc>()
  private val infectedNpcs = mutableSetOf<Npc>()
  val entities = engine.entities.filter { npcMapper.has(it) }

  fun processEntity(entity: Entity) {
    val npc = npcMapper[entity].npc

    when (npc.coronaStatus) {
      CoronaStatus.Susceptible -> doIGetInfected(npc)
      CoronaStatus.Infected -> haveIRecovered(npc)
    }
  }

  private fun initIfNeeded() {
    if (needsInit) {
      needsInit = false
      npcs = entities.map { npcMapper[it]!!.npc }
      infectedNpcs.addAll(npcs.filter { it.coronaStatus == CoronaStatus.Infected })
      CoronaStats.infected = infectedNpcs.count()
      CoronaStats.asymptomatic = infectedNpcs.count { !it.symptomatic }
      CoronaStats.susceptible = npcs.count { it.coronaStatus == CoronaStatus.Susceptible }
    }
  }

  private fun haveIRecovered(npc: Npc) {
    /*
    Recovery takes some time
     */
    val period = Period.between(npc.infectionDate, AiAndTimeSystem.currentDateTime.toLocalDate()).days
    val range = 0..100

    //After FIVE days you will be symptomatic, at the latest
    if (period > CoronaStats.SymptomaticLimit && !npc.symptomatic) {
      npc.symptomatic = true
      CoronaStats.asymptomatic--
    }

    if (npc.symptomatic && !npc.iWillStayAtHome) {
      if (range.random() > 25) {
        npc.iWillStayAtHome = true
        CoronaStats.symptomaticThatStayAtHome++
      }
    }

    if (period > CoronaStats.RecoveryLimit) {
      val r = range.random()
      if (r < CoronaStats.ChanceOfDeath) {
        npc.coronaStatus = CoronaStatus.Dead
        infectedNpcs.remove(npc)
        CoronaStats.infected-- //All that die are symptomatic
        CoronaStats.dead++

        if (npc.iWillStayAtHome)
          CoronaStats.symptomaticThatStayAtHome--
      } else if (r < CoronaStats.ChangeOfRecovery) {
        npc.coronaStatus = CoronaStatus.Recovered
        infectedNpcs.remove(npc)
        CoronaStats.infected--
        CoronaStats.recovered++
        if (npc.iWillStayAtHome)
          CoronaStats.symptomaticThatStayAtHome--
      }
    }
  }

  private fun doIGetInfected(npc: Npc) {
    val circle = Circle(npc.currentPosition, 3f)
    val infectionRisk = infectedNpcs.count { circle.contains(it.currentPosition) }.toFloat().coerceAtMost(3f)
    if (infectionRisk > 0f) {
      if ((0f..100f).random() < infectionRisk) {
        infectedNpcs.add(npc)
        npc.coronaStatus = CoronaStatus.Infected

        CoronaStats.susceptible--
        CoronaStats.infected++
        if ((0f..100f).random() < 25f) {
          CoronaStats.asymptomatic++
          npc.symptomatic = false
        }
      }
    }
  }
}

