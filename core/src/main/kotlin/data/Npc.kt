package data

import ai.npc.TravelMode
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Queue
import ktx.math.vec2
import screens.Mgo
import screens.Place
import screens.PlaceType
import statemachine.StateMachine
import systems.AiAndTimeSystem
import java.time.LocalDate

/**
 * We're gonna be needin' some friendship up in here.
 */
class Npc(val name: String, val id: String, homeArea: Rectangle, val walkingRange: Float = 100f) {
    lateinit var behaviorTree: BehaviorTree<Npc>
    var currentNeed: String = Needs.Money
    var iWillStayAtHome = false
    var symptomatic = true
    lateinit var thePlaceIWantToBe: Pair<Place, TravelMode>
        private set
    val workPlace = Mgo.workPlaces.random()
    val home = Place(type = PlaceType.Home, box = homeArea)
    val friends = mutableSetOf<Npc>()
    private val circleOfConcernRadius = 4f

    var infectionDate = LocalDate.of(2020,1,1)

    var coronaStatus = CoronaStatus.Susceptible
        set(value) {
            infectionDate = AiAndTimeSystem.currentDateTime.toLocalDate()
            field = value
        }

    var currentPosition: Vector2 = vec2(0f,0f)
    private val _circleOfConcern = Circle(currentPosition.x, currentPosition.y, circleOfConcernRadius)
    val circleOfConcern : Circle get() {
        _circleOfConcern.set(currentPosition, circleOfConcernRadius)
        return _circleOfConcern
    }

    val onTheMove get() = npcState in NeedsAndStuff.movingStates
    val meetingAFriend get() = npcState == Activity.GoingToMeetAFriend && friendToGoTo != null
    var friendToGoTo : Npc? = null

    var isDead = false
        private set

    var npcState: String = Activity.Neutral
        private set

    val npcStats = NpcStats()
//            .apply {
//        statsMap[Needs.Fuel] = Stats.range.random()
//        statsMap[Needs.Rest] = Stats.range.random()
//        statsMap[Needs.Money] = Stats.range.random()
//        statsMap[Needs.Social] = Stats.range.random()
//    }

    private val npcStateMachine = StateMachine.buildStateMachine(Activity.Neutral, ::myStateHasChanged) {
        state(Activity.Neutral) {
            edge(Events.LeftSomewhere, Activity.OnTheMove) {}
            edge(Events.GoingToMeetAFriend, Activity.GoingToMeetAFriend) {}
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
        state(Activity.GoingToMeetAFriend) {
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

    val status: String get() {
        return """
            $name
            ${behaviorTree.status}
            $npcState
            $npcStats
        """.trimIndent()
    }

    override fun toString(): String {
        return status
    }


    private fun myStateHasChanged(newState: String) {
        npcState = newState
    }

    fun stopDoingIt() {
        if (npcState != Activity.Neutral)
            npcStateMachine.acceptEvent(Events.StoppedDoingIt)
    }

    /**
     * This takes the first place-pair
     * in the queue and travels there.
     */
    fun travelToFirstPlace() {
        thePlaceIWantToBe = placesToGoTo.first()
        npcStateMachine.acceptEvent(Events.LeftSomewhere)
    }



    fun die() {
        isDead
    }

    fun acceptEvent(event: String) {
        npcStateMachine.acceptEvent(event)
    }

    fun goToMeet(friend: Npc) {
        friendToGoTo = friend

    }

    val placesToGoTo = Queue<Pair<Place, TravelMode>>()
    fun addPlaceToGoTo(place: Place, mode: TravelMode) {
        placesToGoTo.addFirst(Pair(place, mode))
    }
}


