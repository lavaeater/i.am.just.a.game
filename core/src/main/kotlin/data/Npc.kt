package data

import com.badlogic.gdx.math.Vector2
import screens.Mgo
import screens.Place
import statemachine.StateMachine

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


