package data

import ktx.log.info
import ktx.math.amid
import ktx.math.random
import ktx.math.vec2
import statemachine.StateMachine

/**
 * Sort of like what we're doing or something
 */
enum class States {
    Neutral,
    OnTheMove,
    Eating,
    Sleeping,
    Working,
    HavingFun,
    Socializing
}

enum class Events {
    StartedEating,
    StoppedEating,
    FellAsleep,
    WokeUp,
    HadSomeFun,
    MadeSomeMoney,
    StartedWorking,
    MetSomePeople,
    LeftSomewhere,
    Arrived,
    StartedHavingFun,
    StartedSocializing
}

object RR {
    val statsR = 64 amid 32
}

class Npc(val name: String, val id: String) {
    var isDead = false
        private set
    var npcState: States = States.Neutral
        private set
    private var npcStats = NpcStats(RR.statsR.random(), RR.statsR.random(),RR.statsR.random(),RR.statsR.random(),RR.statsR.random())

    var thisIsWhereIWantToBe = vec2(0f, 0f)
        private set

    private val _npcNeeds = mutableSetOf<NpcNeed>()
    private val npcNeeds: List<NpcNeed>
        get() {
            return _npcNeeds.toList().sortedBy { it.priority }
        }

    private val npcStateMachine = StateMachine.buildStateMachine<States, Events>(States.Neutral, ::myStateHasChanged, {
        state(States.Neutral) {
            edge(Events.LeftSomewhere, States.OnTheMove) {}
            edge(Events.FellAsleep, States.Sleeping) {}
            edge(Events.StartedEating, States.Eating) {}
            edge(Events.StartedHavingFun, States.HavingFun) {}
            edge(Events.StartedSocializing, States.Socializing) {}
            edge(Events.StartedWorking, States.Working) {}
        }
        state(States.OnTheMove) {
            edge(Events.Arrived, States.Neutral) {}
            edge(Events.FellAsleep, States.Sleeping) {}
            edge(Events.StartedEating, States.Eating) {}
            edge(Events.StartedHavingFun, States.Eating) {}
            edge(Events.StartedWorking, States.Eating) {}
            edge(Events.StartedSocializing, States.Eating) {}
        }
        state(States.HavingFun) {
            edge(Events.FellAsleep, States.Sleeping) {}
            edge(Events.StartedEating, States.Eating) {}
            edge(Events.HadSomeFun, States.Neutral) {}
        }
        state(States.Working) {
            edge(Events.FellAsleep, States.Sleeping) {}
            edge(Events.StartedEating, States.Eating) {}
            edge(Events.MadeSomeMoney, States.Neutral) {}
        }
        state(States.Socializing) {
            edge(Events.FellAsleep, States.Sleeping) {}
            edge(Events.StartedEating, States.Eating) {}
            edge(Events.MetSomePeople, States.Neutral) {}
        }
        state(States.Eating) {
            edge(Events.StoppedEating, States.Neutral) {}
            edge(Events.FellAsleep, States.Sleeping) {}
        }
        state(States.Sleeping) {
            edge(Events.WokeUp, States.Neutral) {}
        }
    })

    init {
        npcStateMachine.initialize() //Required to make states possible
        info { "$name is ALIVE! Stats: $npcStats" }
    }


    private fun myStateHasChanged(newState: States) {
        /*
            React to new state, perhaps? Set a property!
             */
        npcState = newState
    }


    fun timeHasPassed(minutesPassed: Long = 15) {
        applyCosts()
        checkNpcNeeds()
    }

    private fun applyCosts() {
        val cost = NpcDataAndStuff.activities.first { it.state == npcState }

        npcStats.fuel =(npcStats.fuel - cost.fuelCost).coerceIn(fullRange)
        npcStats.rested =(npcStats.rested - cost.restCost).coerceIn(fullRange)
        npcStats.money =(npcStats.money - cost.moneyCost).coerceIn(fullRange)
        npcStats.boredom =(npcStats.boredom - cost.boredomCost).coerceIn(fullRange)
        npcStats.social =(npcStats.social - cost.socialCost).coerceIn(fullRange)
    }

    private fun checkNpcNeeds() {

        /*
            The NPC can ever only be in one state.

            The AI System is there for the NPC to select what he
            will do next, based on the data he has about his own status.

            How will that work, really?

            The wrong idea here is that an Npc can only be one thing


            The NPC can be many things - but he can only DO one thing at
            a time.

            So, the states have to be modified.

            States can be several at a time, one can be hungry, starving, whatever.

            These things only affect what you MIGHT do. So how do we use that
            for the behavior tree, really?

            We should read the NPC Stats to the NpcTasks and use them
            for the odds of the NPC doing something. So "hungry" is no longer a
            state one is in, it is a statement of FACT.

            The state machine should be updated by the AI System only, I suppose,
            to keep track of what the NPC is doing, not how he's feeling.
             */

        when(npcStats.fuel) {
            in lowRange -> _npcNeeds.add(NpcDataAndStuff.needs[Needs.Fuel]!!)
            in greatRange -> _npcNeeds.remove(NpcDataAndStuff.needs[Needs.Fuel]!!)
        }

        when(npcStats.rested) {
            in lowRange -> _npcNeeds.add(NpcDataAndStuff.needs[Needs.Rest]!!)
            in greatRange -> _npcNeeds.remove(NpcDataAndStuff.needs[Needs.Rest]!!)
        }

        when(npcStats.money) {
            in lowRange -> _npcNeeds.add(NpcDataAndStuff.needs[Needs.Money]!!)
            in greatRange -> _npcNeeds.remove(NpcDataAndStuff.needs[Needs.Money]!!)
        }

        when(npcStats.boredom) {
            in lowRange -> _npcNeeds.add(NpcDataAndStuff.needs[Needs.Fun]!!)
            in greatRange -> _npcNeeds.remove(NpcDataAndStuff.needs[Needs.Fun]!!)
        }

        when(npcStats.social) {
            in lowRange -> _npcNeeds.add(NpcDataAndStuff.needs[Needs.Social]!!)
            in greatRange -> _npcNeeds.remove(NpcDataAndStuff.needs[Needs.Social]!!)
        }
        //Die
        if(npcStats.fuel <= lowRange.first + 1) {
            info { "$name just died. Geez. Stats: $npcStats"}
            isDead = true
        }
    }

    private val lowRange = -24..24
    private val normalRange = 25..72
    private val greatRange = 73..128 //Max is more like close to 96, but wealth can take us higher, and drugs and so on
    private val goodRange = normalRange.first..greatRange.last
    private val fullRange = lowRange.first..goodRange.last

    fun goSomeWhere() {
        if (npcState == States.Neutral) {
            val r = 0f amid 500f
            thisIsWhereIWantToBe = vec2(r.random(), r.random())
            npcStateMachine.acceptEvent(Events.LeftSomewhere)
        }
    }

    fun arrivedSomewhere() {
        if(npcState == States.OnTheMove)
            npcStateMachine.acceptEvent(Events.Arrived)
    }

    fun startSleeping() {
        if(npcState != States.Sleeping)
            npcStateMachine.acceptEvent(Events.FellAsleep)
    }

    fun stopSleeping() {
        if(npcState == States.Sleeping) {
            npcStateMachine.acceptEvent(Events.WokeUp)
        }
    }

    fun hasNeed(need: Needs) : Boolean {
        return npcNeeds.has(need)
    }

    fun startEating() {
        if(npcState != States.Eating) {
            npcStateMachine.acceptEvent(Events.StartedEating)
        }
    }

    fun stopEating() {
        if(npcState == States.Eating)
            npcStateMachine.acceptEvent(Events.StoppedEating)
    }

    fun hasAnyNeeds(): Boolean {
        return npcNeeds.any()
    }

    fun log() {
        info { "$name is $npcState, stats: $npcStats, needs: $npcNeeds" }
    }

    fun startWorking() {
        if(npcState != States.Working) {
            npcStateMachine.acceptEvent(Events.StartedWorking)
        }
    }
    fun stopWorking() {
        if(npcState == States.Working)
            npcStateMachine.acceptEvent(Events.MadeSomeMoney)
    }

    fun startSocializing() {
        if(npcState != States.Socializing) {
            npcStateMachine.acceptEvent(Events.StartedSocializing)
        }
    }
    fun stopSocializing() {
        if(npcState == States.Socializing)
            npcStateMachine.acceptEvent(Events.MetSomePeople)
    }

    fun startHavingFun() {
        if(npcState != States.HavingFun) {
            npcStateMachine.acceptEvent(Events.StartedHavingFun)
        }
    }
    fun stopHavingFun() {
        if(npcState == States.HavingFun)
            npcStateMachine.acceptEvent(Events.HadSomeFun)
    }

}


data class NpcStats(var fuel: Int = 72, var rested: Int = 72, var money: Int = 72, var social: Int = 72, var boredom: Int = 72)
class NpcActivity(
        val state: States,
        private val fuel: Int = 4,
        private val rest:Int =4,
        private val money:Int = 0,
        private val social:Int = 4,
        private val boredom:Int = 4) {
    val fuelCost: Int get() = (fuel amid kotlin.math.abs(fuel / 2)).random()
    val restCost: Int get() = (rest amid kotlin.math.abs(rest / 2)).random()
    val moneyCost: Int get() = (money amid kotlin.math.abs(money / 2)).random()
    val socialCost: Int get() = (social amid kotlin.math.abs(social / 2)).random()
    val boredomCost: Int get() = (boredom amid kotlin.math.abs(boredom / 2)).random()
}

object NpcDataAndStuff {
    val activities = listOf(
            NpcActivity(States.HavingFun, fuel = 6, boredom = -12),
            NpcActivity(States.Socializing, fuel = 6, money = 2, social = -12, boredom = -4, rest = 2),
            NpcActivity(States.Working, fuel = 6, money = -16, social = -4, boredom = 8, rest = 8),
            NpcActivity(States.Sleeping, fuel = 0, social = 2, boredom = 0, rest = -16),
            NpcActivity(States.Eating, fuel = -16, social = -4, boredom = -2, rest = -2, money = 16),
            NpcActivity(States.Neutral),
            NpcActivity(States.OnTheMove, fuel = 8, rest = 8, social = -2, boredom = -2)
    )

    val needs = mapOf(
            Needs.Fuel to NpcNeed(Needs.Fuel, 1),
            Needs.Rest to NpcNeed(Needs.Rest, 2),
            Needs.Money to NpcNeed(Needs.Money, 3),
            Needs.Fun to NpcNeed(Needs.Fun, 4),
            Needs.Social to NpcNeed(Needs.Social, 4)
    )
}


enum class Needs {
    Fuel,
    Rest,
    Money,
    Social,
    Fun
}

data class NpcNeed(val need: Needs, val priority: Int)

fun List<NpcNeed>.has(need: Needs, topNeed: Boolean = true) : Boolean {
    return if(topNeed) this.firstOrNull()?.need == need else this.any { it.need == need }
}
