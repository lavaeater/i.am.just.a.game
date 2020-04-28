package data

import ai.npc.TravelMode
import ai.pathfinding.StarIsBorn
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Queue
import dst2
import graph.Node
import ktx.math.ImmutableVector2
import ktx.math.random
import ktx.math.toMutable
import screens.Mgo
import statemachine.StateMachine
import systems.AiAndTimeSystem
import java.time.LocalDate

/**
 * We're gonna be needin' some friendship up in here.
 */
class Npc(val name: String, val id: String, val home: Place, val workPlace: Place, val walkingRange: Float = 100f) {
    val speed = (1f..3f).random()
    lateinit var currentPath: Pair<Node<ImmutableVector2>, MutableList<Node<ImmutableVector2>>>
    lateinit var behaviorTree: BehaviorTree<Npc>
    var currentNeed: String = Needs.Money
    var iWillStayAtHome = false
    var symptomatic = true
    lateinit var thePlaceIWantToBe: Pair<Place, TravelMode>
        private set
    val friends = mutableSetOf<Npc>()
    private val circleOfConcernRadius = 4f

    var infectionDate = LocalDate.of(2020,1,1)

    var coronaStatus = CoronaStatus.Susceptible
        set(value) {
            infectionDate = AiAndTimeSystem.currentDateTime.toLocalDate()
            field = value
        }

    var currentPosition: Vector2 = home.center.toMutable()
    set(value) {
        field.set(value)
    }
    private val _circleOfConcern = Circle(currentPosition.x, currentPosition.y, circleOfConcernRadius)
    val circleOfConcern : Circle get() {
        _circleOfConcern.set(currentPosition, circleOfConcernRadius)
        return _circleOfConcern
    }

    val onTheMove get() = npcState in Needs.movingStates
    val meetingAFriend get() = npcState == Activities.GoingToMeetAFriend && friendToGoTo != null
    var friendToGoTo : Npc? = null

    var isDead = false
        private set

    var npcState: String = Activities.Neutral
        private set

    val npcStats = NpcStats()
            .apply {
        statsMap[Needs.Fuel] = 0
        statsMap[Needs.Rest] = 96
        statsMap[Needs.Money] = 96
        statsMap[Needs.Social] = 94
        statsMap[Needs.Fun] = 94
    }

    private val npcStateMachine = StateMachine.buildStateMachine(Activities.Neutral, ::myStateHasChanged) {
        state(Activities.Neutral) {
            edge(Events.LeftSomewhere, Activities.OnTheMove) {}
            edge(Events.GoingToMeetAFriend, Activities.GoingToMeetAFriend) {}
            edge(Events.FellAsleep, Activities.Sleeping) {}
            edge(Events.StartedEating, Activities.Eating) {}
            edge(Events.WentToEat, Activities.GoingToEat) {}
            edge(Events.WentToWork, Activities.GoingToWork) {}
            edge(Events.WentHomeToSleep, Activities.GoingHomeToSleep) {}
            edge(Events.StartedHavingFun, Activities.HavingFun) {}
            edge(Events.StartedSocializing, Activities.Socializing) {}
            edge(Events.StartedWorking, Activities.Working) {}
        }
        state(Activities.OnTheMove) {
            edge(Events.StoppedDoingIt, Activities.Neutral) {}
        }
        state(Activities.GoingToMeetAFriend) {
            edge(Events.StoppedDoingIt, Activities.Neutral) {}
        }
        state(Activities.HavingFun) {
            edge(Events.StoppedDoingIt, Activities.Neutral) {}
        }
        state(Activities.Working) {
            edge(Events.StoppedDoingIt, Activities.Neutral) {}
        }
        state(Activities.Socializing) {
            edge(Events.StoppedDoingIt, Activities.Neutral) {}
        }
        state(Activities.Eating) {
            edge(Events.StoppedDoingIt, Activities.Neutral) {}
            edge(Events.LeftSomewhere, Activities.OnTheMove) {}
        }
        state(Activities.GoingToEat) {
            edge(Events.StoppedDoingIt, Activities.Neutral) {}
        }
        state(Activities.GoingToWork) {
            edge(Events.StoppedDoingIt, Activities.Neutral) {}
        }
        state(Activities.GoingHomeToSleep) {
            edge(Events.StoppedDoingIt, Activities.Neutral) {}
        }
        state(Activities.Sleeping) {
            edge(Events.StoppedDoingIt, Activities.Neutral) {}
        }
    }

    init {
        npcStateMachine.initialize() //Required to make states possible
    }

    val status: String get() {
        return """
            $name
            Status: $coronaStatus
            Symptomatic: $symptomatic
            Stays at home: $iWillStayAtHome
            State: $npcState
            Current Top Need: $currentNeed 
            Fuel: ${npcStats.statsMap[Needs.Fuel]}
            Rest: ${npcStats.statsMap[Needs.Rest]}
            Money: ${npcStats.statsMap[Needs.Money]}
        """.trimIndent()
    }

    override fun toString(): String {
        return status
    }


    private fun myStateHasChanged(newState: String) {
        npcState = newState
    }

    fun stopDoingIt() {
        if (npcState != Activities.Neutral)
            npcStateMachine.acceptEvent(Events.StoppedDoingIt)
    }

    /**
     * This takes the first place-pair
     * in the queue and travels there.
     */
    fun travelToFirstPlace() {
        if(!::thePlaceIWantToBe.isInitialized || placesToGoTo.first() != thePlaceIWantToBe) {
            thePlaceIWantToBe = placesToGoTo.first()

            if(thePlaceIWantToBe.second == TravelMode.Walking && (!::currentPath.isInitialized || currentPath.first != thePlaceIWantToBe.first.node)) {
                //Find the currently closest node
                val start = Mgo.graph.nodes.minBy { it.data.dst2(currentPosition) }!! //Potentially slow, might wanna do this some other way
                val goal = thePlaceIWantToBe.first.node
                currentPath = Pair(goal, StarIsBorn.calculatePath(start, goal, ::cost))
                //So, we have a path from where we are to where we want to go.
            }
            npcStateMachine.acceptEvent(Events.LeftSomewhere)
        }
    }


    fun cost(from: Node<ImmutableVector2>, to: Node<ImmutableVector2>): Int {
        return from.data.dst2(to.data).toInt()
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



