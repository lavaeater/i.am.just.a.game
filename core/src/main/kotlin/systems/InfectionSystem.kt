package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.math.Circle
import components.NpcComponent
import data.CoronaStatus
import data.Npc
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.log.info
import ktx.math.random
import java.time.Period


class CoronaStats {
  companion object {
    var infected = 0
    var susceptible = 0
    var dead = 0
    var recovered = 0
    var asymptomatic = 0
    var symptomaticThatStayAtHome = 0
    const val SymptomaticLimit = 5
    const val RecoveryLimit = 13
    const val ChanceOfDeath = 4
    const val ChangeOfRecovery = 15
  }
}



/**
 * Interval should be same as in Ai / Time system - no, it should be 4 times that!
 */
class InfectionSystem(interval: Float = 5f) : IntervalIteratingSystem(allOf(NpcComponent::class).get(), interval, 5) {
  private var needsInit = true
  private val npcMapper = mapperFor<NpcComponent>()
  var npcs = listOf<Npc>()
  val infectedNpcs = mutableSetOf<Npc>()

  override fun processEntity(entity: Entity) {
    val npc = npcMapper[entity].npc

    when(npc.coronaStatus) {
      CoronaStatus.Susceptible -> doIGetInfected(npc)
      CoronaStatus.Infected -> haveIRecovered(npc)
    }



  }

  override fun updateInterval() {
    initIfNeeded()
    super.updateInterval()
  }

  private fun initIfNeeded() {
    if(needsInit) {
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
    if(period > CoronaStats.SymptomaticLimit) {
      npc.symptomatic = true
      CoronaStats.asymptomatic--

      //Will I stay at home?
      if(range.random() > 25) {
        npc.iWillStayAtHome = true
        CoronaStats.symptomaticThatStayAtHome++
      }
    }


    if(period > CoronaStats.RecoveryLimit) {
      val r = range.random()
      if (r < CoronaStats.ChanceOfDeath) {
        npc.coronaStatus = CoronaStatus.Dead
        infectedNpcs.remove(npc)
        CoronaStats.infected-- //All that die are symptomatic
        if(npc.iWillStayAtHome)
          CoronaStats.symptomaticThatStayAtHome--
      } else if (r < CoronaStats.ChangeOfRecovery) {
        npc.coronaStatus = CoronaStatus.Recovered
        infectedNpcs.remove(npc)
        CoronaStats.infected--
        CoronaStats.recovered++
        if(npc.iWillStayAtHome)
          CoronaStats.symptomaticThatStayAtHome--
      }
    }
  }


  fun doIGetInfected(npc: Npc) {
    //Find all npc's within a certain radius...
    //Range 3m
    if(!npc.onTheMove) {
      val circle = Circle(npc.currentPosition, 3f)
      val infectionRisk = infectedNpcs.count { circle.contains(it.currentPosition) }.toFloat().coerceAtMost(1f)
      if (infectionRisk > 0f) {
        var dieRoll = (0f..100f).random()
        if (dieRoll < infectionRisk) {
          infectedNpcs.add(npc)
          npc.coronaStatus = CoronaStatus.Infected

          CoronaStats.susceptible--
          CoronaStats.infected++
          dieRoll = (0f..100f).random()
          if (dieRoll < 25f) {
            CoronaStats.asymptomatic++
            npc.symptomatic = false
          }
        }
      }
    }
  }
}