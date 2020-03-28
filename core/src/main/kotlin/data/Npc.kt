package data

import com.badlogic.gdx.math.Vector2
import ktx.math.amid
import screens.Mgo
import screens.Place
import statemachine.StateMachine

/**
 * Sort of like what we're doing or something
 */
enum class Activity {
    Neutral,
    OnTheMove,
    Eating,
    Sleeping,
    Working,
    HavingFun,
    Socializing,
    GoingToEat,
    GoingToWork,
    GoingHomeToSleep
}

enum class Events {
    StartedEating,
    FellAsleep,
    StartedWorking,
    LeftSomewhere,
    StartedHavingFun,
    StartedSocializing,
    StoppedDoingIt,
    WentToEat,
    WentToWork,
    WentHomeToSleep
}

object RR {
    val statsR = 64 amid 32
}

class Npc(val name: String, val id: String) {
    lateinit var thePlaceIWantToBe: Place
        private set
    val workPlace = Mgo.workPlaces.random()
    val home = Mgo.homeAreas.random()
    lateinit var currentPosition: Vector2

    val onTheMove get() = npcState in NeedsAndStuff.movingStates

    var isDead = false
        private set

    var npcState: Activity = Activity.Neutral
        private set

    private val npcStats = NpcStats().apply {
        statsMap[Needs.Fuel] =  RR.statsR.random()
        statsMap[Needs.Rest] =  RR.statsR.random()
        statsMap[Needs.Money] =  RR.statsR.random()
    }

    private val npcStateMachine = StateMachine.buildStateMachine<Activity, Events>(Activity.Neutral, ::myStateHasChanged, {
        state(Activity.Neutral) {
            edge(Events.LeftSomewhere, Activity.OnTheMove) {}
            edge(Events.FellAsleep, Activity.Sleeping) {}
            edge(Events.StartedEating, Activity.Eating) {}
            edge(Events.WentToEat, Activity.GoingToEat) {}
            edge(Events.WentToWork, Activity.GoingToWork) {}
            edge(Events.WentHomeToSleep, Activity.GoingHomeToSleep) {}
            edge(Events.StartedHavingFun, Activity.HavingFun) {}
            edge(Events.StartedSocializing, Activity.Socializing) {}
            edge(Events.StartedWorking, Activity.Working) {}
        }
        state(Activity.OnTheMove) {
            edge(Events.StoppedDoingIt, Activity.Neutral) {}
        }
        state(Activity.HavingFun) {
            edge(Events.StoppedDoingIt, Activity.Neutral) {}
        }
        state(Activity.Working) {
            edge(Events.StoppedDoingIt, Activity.Neutral) {}
        }
        state(Activity.Socializing) {
            edge(Events.StoppedDoingIt, Activity.Neutral) {}
        }
        state(Activity.Eating) {
            edge(Events.StoppedDoingIt, Activity.Neutral) {}
        }
        state(Activity.GoingToEat) {
            edge(Events.StoppedDoingIt, Activity.Neutral) {}
        }
        state(Activity.GoingToWork) {
            edge(Events.StoppedDoingIt, Activity.Neutral) {}
        }
        state(Activity.GoingHomeToSleep) {
            edge(Events.StoppedDoingIt, Activity.Neutral) {}
        }
        state(Activity.Sleeping) {
            edge(Events.StoppedDoingIt, Activity.Neutral) {}
        }
    })

    init {
        npcStateMachine.initialize() //Required to make states possible
    }


    private fun myStateHasChanged(newState: Activity) {
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
        val cost = NeedsAndStuff.activities[npcState] ?: error("No activity found")
        applyCosts(cost)
    }

    fun applyCosts(cost: Cost) {

        for((k, c) in cost.costMap) {
            npcStats.statsMap[k] = (npcStats.statsMap[k]!! - c).coerceIn(fullRange)
        }
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
//
//        when(npcStats.fuel) {
//            in lowRange -> _npcNeeds.add(NeedsAndStuff.needs[Needs.Fuel]!!)
//            in greatRange -> _npcNeeds.remove(NeedsAndStuff.needs[Needs.Fuel]!!)
//        }
//
//        when(npcStats.rested) {
//            in lowRange -> _npcNeeds.add(NeedsAndStuff.needs[Needs.Rest]!!)
//            in greatRange -> _npcNeeds.remove(NeedsAndStuff.needs[Needs.Rest]!!)
//        }
//
//        when(npcStats.money) {
//            in lowRange -> _npcNeeds.add(NeedsAndStuff.needs[Needs.Money]!!)
//            in greatRange -> _npcNeeds.remove(NeedsAndStuff.needs[Needs.Money]!!)
//        }

        /*
        If the npc's base need always is boredom, he / she will always do something
        Nifty.
         */
//        when(npcStats.boredom) {
//            in fullRange -> _npcNeeds.add(NeedsAndStuff.needs[Needs.Fun]!!)
//        }

//        when(npcStats.social) {
//            in lowRange -> _npcNeeds.add(NeedsAndStuff.needs[Needs.Social]!!)
//            in greatRange -> _npcNeeds.remove(NeedsAndStuff.needs[Needs.Social]!!)
//        }
        //Die
        if(npcStats.statsMap[Needs.Fuel]!! <= lowRange.first + 1) {
            isDead = true
        }
    }

    private val lowRange = -24..24
    private val normalRange = 25..72
    private val greatRange = 73..128 //Max is more like close to 96, but wealth can take us higher, and drugs and so on
    private val goodRange = normalRange.first..greatRange.last
    private val fullRange = lowRange.first..goodRange.last

    fun stopDoingIt() {
        if(npcState != Activity.Neutral)
            npcStateMachine.acceptEvent(Events.StoppedDoingIt)
    }


    fun hasNeed(need: String) : Boolean {
        return false
    }


    fun walkTo(placeToGo: Place) {
        thePlaceIWantToBe = placeToGo
        npcStateMachine.acceptEvent(Events.LeftSomewhere)
    }
}

data class NpcStats(val statsMap: MutableMap<String, Int> = mutableMapOf(
        Needs.Fuel to 72,
        Needs.Money to 72,
        Needs.Rest to 72
))

data class Cost(val activity: Activity, val costMap: Map<String, Int> = mapOf(
        Needs.Fuel to 4,
        Needs.Rest to 4,
        Needs.Money to 4
))

class NeedsAndStuff {
    companion object NpcDataAndStuff {
        val activities = mapOf(
                Activity.Working to Cost(Activity.Working, mapOf(
                    Needs.Money to -16,
                    Needs.Fuel to 8,
                    Needs.Rest to 6
                )),
                Activity.Sleeping to Cost(Activity.Sleeping, mapOf(
                Needs.Money to -16,
                Needs.Fuel to 8,
                Needs.Rest to 6
        )),
                Activity.Eating to Cost(Activity.Eating, mapOf(
        Needs.Money to -16,
        Needs.Fuel to 8,
        Needs.Rest to 6
        )),
                Activity.Neutral to Cost(Activity.Neutral),
                Activity.OnTheMove to Cost(Activity.OnTheMove, mapOf(
        Needs.Money to -16,
        Needs.Fuel to 8,
        Needs.Rest to 6)))

        val needsToActivities = mapOf(
                Needs.Fuel to Activity.Eating,
                Needs.Rest to Activity.Sleeping,
                Needs.Money to Activity.Working
        )

        val statesToNeeds = mapOf(
                Activity.Working to Needs.Money,
                Activity.Sleeping to Needs.Rest,
                Activity.Eating to Needs.Fuel
        )

        val movingStates = setOf(
                Activity.OnTheMove,
                Activity.GoingToEat,
                Activity.GoingToWork,
                Activity.GoingHomeToSleep
        )
    }
}


class Needs {
    companion object {
        const val Fuel = "Fuel"
        const val Rest ="Rest"
        const val Money = "Money"
    }
}
