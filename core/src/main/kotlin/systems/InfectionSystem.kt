package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.math.Circle
import components.NpcComponent
import data.Activity
import data.CoronaStatus
import data.Npc
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.log.info
import ktx.math.random
import java.time.Period

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

    if(npc.npcState != Activity.OnTheMove)
      when(npc.coronaStatus) {
        CoronaStatus.Susceptible -> doIGetInfected(npc)
        CoronaStatus.Infected -> haveIRecovered(npc)
      }
  }

  var minutesPassed : Long= 0
  var daysPassed = 0

  override fun updateInterval() {
    initIfNeeded()

    minutesPassed += AiAndTimeSystem.minutesPerTick
    if(minutesPassed % 720 == 0L) {
      npcs = entities.map { npcMapper[it]!!.npc }
      val asymptomatic = infectedNpcs.count { !it.symptomatic }
      val recovered = npcs.filter { it.coronaStatus == CoronaStatus.Recovered }.count()
      val susceptible = npcs.filter { it.coronaStatus == CoronaStatus.Susceptible }.count()
      val dead = npcs.filter { it.coronaStatus == CoronaStatus.Dead }.count()
      info { "Day $daysPassed. ${infectedNpcs.count()} infected, $asymptomatic without symptoms. \n$recovered recovered and $susceptible still at risk. \n$dead deaths - so far" }
    }

    if(minutesPassed % 1440 == 0L)
      daysPassed++

    super.updateInterval()
  }

  private fun initIfNeeded() {
    if(needsInit) {
      needsInit = false
      infectedNpcs.addAll(entities.map{ npcMapper[it]!!.npc }.filter { it.coronaStatus == CoronaStatus.Infected })
    }
  }

  private fun haveIRecovered(npc: Npc) {
    /*
    Recovery takes some time
     */
    val period = Period.between(npc.infectionDate, AiAndTimeSystem.currentDateTime.toLocalDate()).days
    val range = 0..100

    //After FIVE days you will be symptomatic, at the latest
    if(period > 5) {
      npc.symptomatic = true
      if(range.random() > 25) {
        npc.iWillStayAtHome = true
      }
    }

    //Will I stay at home?


    if(period > 13) {
      if (range.random() < 4) {
        npc.coronaStatus = CoronaStatus.Dead
      } else {
        npc.coronaStatus = CoronaStatus.Recovered
      }
      infectedNpcs.remove(npc)
    }
  }


  fun doIGetInfected(npc: Npc) {
    //Find all npc's within a certain radius...
    //Range 3m
    val circle = Circle(npc.currentPosition, 3f)
    val infectionRisk = infectedNpcs.count { circle.contains(it.currentPosition) }.toFloat() * 2f
    if(infectionRisk > 0f) {
      var dieRoll = (0f..100f).random()
      if (dieRoll < infectionRisk) {
        infectedNpcs.add(npc)
        npc.coronaStatus = CoronaStatus.Infected
        dieRoll = (0f..100f).random()
        if (dieRoll < 25f)
          npc.symptomatic = false
      }
    }
  }
}