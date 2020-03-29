package data

import com.badlogic.gdx.math.Vector2
import ktx.math.amid
import screens.Mgo
import screens.Place
import statemachine.StateMachine

/**
 * Sort of like what we're doing or something
 */
class Activity {
    companion object {
        const val Neutral = "Neutral"
        const val OnTheMove = "OnTheMove"
        const val Eating = "Eating"
        const val Sleeping = "Sleeping"
        const val Working = "Working"
        const val HavingFun = "HavingFun"
        const val Socializing = "Socializing"
        const val GoingToEat = "GoingToEat"
        const val GoingToWork = "GoingToWork"
        const val GoingHomeToSleep = "GoingHomeToSleep"
    }
}

class Events {
    companion object {
        const val StartedEating = "StartedEating"
        const val FellAsleep = "FellAsleep"
        const val StartedWorking = "StartedWorking"
        const val LeftSomewhere = "LeftSomewhere"
        const val StartedHavingFun = "StartedHavingFun"
        const val StartedSocializing = "StartedSocializing"
        const val StoppedDoingIt = "StoppedDoingIt"
        const val WentToEat = "WentToEat"
        const val WentToWork = "WentToWork"
        const val WentHomeToSleep = "WentToHomeToSleep"
    }
}

class Stats {
    companion object {
        val range = 64 amid 32
    }
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

    var npcState: String = Activity.Neutral
        private set

    val npcStats = NpcStats().apply {
        statsMap[Needs.Fuel] = Stats.range.random()
        statsMap[Needs.Rest] = Stats.range.random()
        statsMap[Needs.Money] = Stats.range.random()
    }

    private val npcStateMachine = StateMachine.buildStateMachine(Activity.Neutral, ::myStateHasChanged) {
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
    }

    init {
        npcStateMachine.initialize() //Required to make states possible
    }


    private fun myStateHasChanged(newState: String) {
        npcState = newState
    }

    fun stopDoingIt() {
        if (npcState != Activity.Neutral)
            npcStateMachine.acceptEvent(Events.StoppedDoingIt)
    }

    fun walkTo(placeToGo: Place) {
        thePlaceIWantToBe = placeToGo
        npcStateMachine.acceptEvent(Events.LeftSomewhere)
    }

    fun die() {
        isDead
    }

    fun acceptEvent(event: String) {
        npcStateMachine.acceptEvent(event)
    }
}

data class NpcStats(val statsMap: MutableMap<String, Int> = mutableMapOf(
        Needs.Fuel to 72,
        Needs.Money to 72,
        Needs.Rest to 72
))

data class Cost(val activity: String, val costMap: Map<String, Int> = mapOf(
        Needs.Fuel to 4,
        Needs.Rest to 4,
        Needs.Money to 4
))

class NeedsAndStuff {
    companion object {
        val costs = mapOf(
                Activity.Working to Cost(
                        Activity.Working, mapOf(
                        Needs.Money to -16,
                        Needs.Fuel to 8,
                        Needs.Rest to 6
                )),
                Activity.Sleeping to Cost(
                        Activity.Sleeping, mapOf(
                        Needs.Money to 0,
                        Needs.Fuel to 0,
                        Needs.Rest to -16
                )),
                Activity.Eating to Cost(Activity.Eating, mapOf(
                        Needs.Money to -8,
                        Needs.Fuel to -16,
                        Needs.Rest to 0
                )),
                Activity.Neutral to Cost(Activity.Neutral),
                Activity.OnTheMove to Cost(
                        Activity.OnTheMove, mapOf(
                        Needs.Money to 0,
                        Needs.Fuel to 8,
                        Needs.Rest to 8)))

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

        val activitiesToEvents = mapOf(
                Activity.Working to Events.StartedWorking,
                Activity.Eating to Events.StartedEating,
                Activity.Sleeping to Events.FellAsleep
        )

        val movingStates = setOf(
                Activity.OnTheMove,
                Activity.GoingToEat,
                Activity.GoingToWork,
                Activity.GoingHomeToSleep
        )

        val lowRange = -24..24
        val normalRange = 25..72
        val greatRange = 73..128 //Max is more like close to 96, but wealth can take us higher, and drugs and so on
        val goodRange = normalRange.first..greatRange.last
        val fullRange = lowRange.first..goodRange.last
    }
}


class Needs {
    companion object {
        const val Fuel = "Fuel"
        const val Rest = "Rest"
        const val Money = "Money"
    }
}
