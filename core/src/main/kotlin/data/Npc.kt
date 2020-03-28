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

    private var npcStats = NpcStats(RR.statsR.random(), RR.statsR.random(),RR.statsR.random(),RR.statsR.random(),RR.statsR.random())

    private val _npcNeeds = mutableSetOf<NpcNeed>()
    val npcNeeds: List<NpcNeed>
        get() {
            return _npcNeeds.toList().sortedBy { it.priority }
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

    fun applyCosts(cost: ActivityCost) {
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
            in lowRange -> _npcNeeds.add(NeedsAndStuff.needs[Needs.Fuel]!!)
            in greatRange -> _npcNeeds.remove(NeedsAndStuff.needs[Needs.Fuel]!!)
        }

        when(npcStats.rested) {
            in lowRange -> _npcNeeds.add(NeedsAndStuff.needs[Needs.Rest]!!)
            in greatRange -> _npcNeeds.remove(NeedsAndStuff.needs[Needs.Rest]!!)
        }

        when(npcStats.money) {
            in lowRange -> _npcNeeds.add(NeedsAndStuff.needs[Needs.Money]!!)
            in greatRange -> _npcNeeds.remove(NeedsAndStuff.needs[Needs.Money]!!)
        }

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
        if(npcStats.fuel <= lowRange.first + 1) {
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
        return npcNeeds.has(need)
    }

    fun hasAnyNeeds(): Boolean {
        return npcNeeds.any()
    }

    fun walkTo(placeToGo: Place) {
        thePlaceIWantToBe = placeToGo
        npcStateMachine.acceptEvent(Events.LeftSomewhere)
    }
}

data class NpcStats(var fuel: Int = 72, var rested: Int = 72, var money: Int = 72, var social: Int = 72, var boredom: Int = 72)
class ActivityCost(
        val state: Activity,
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

class NeedsAndStuff {
    companion object NpcDataAndStuff {
        val activities = mapOf(
                Activity.HavingFun to ActivityCost(Activity.HavingFun, fuel = 6, boredom = -12),
                Activity.Socializing to ActivityCost(Activity.Socializing, fuel = 2, money = 2, social = -12, boredom = -2, rest = 2),
                Activity.Working to ActivityCost(Activity.Working, fuel = 6, money = -16, social = 2, boredom = 16, rest = 8),
                Activity.Sleeping to ActivityCost(Activity.Sleeping, fuel = 0, social = 0, boredom = 0, rest = -16),
                Activity.Eating to ActivityCost(Activity.Eating, fuel = -16, social = 0, boredom = 8, rest = -2, money = 16),
                Activity.Neutral to ActivityCost(Activity.Neutral),
                Activity.OnTheMove to ActivityCost(Activity.OnTheMove, fuel = 8, rest = 8, social = 2, boredom = -2),
                Activity.GoingToEat to ActivityCost(Activity.GoingToEat, fuel = 0, rest = 0, social = 0, boredom = 0),
                Activity.GoingToWork to ActivityCost(Activity.GoingToWork, fuel = 0, rest = 0, social = 0, boredom = 0, money = 2),
                Activity.GoingHomeToSleep to ActivityCost(Activity.GoingHomeToSleep, fuel = 0, rest = 0, social = 0, boredom = 0, money = 0)
        )

        val needs = mapOf(
                Needs.Fuel to NpcNeed(Needs.Fuel, 1),
                Needs.Rest to NpcNeed(Needs.Rest, 2),
                Needs.Money to NpcNeed(Needs.Money, 3))

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

data class NpcNeed(val key: String, val priority: Int)

fun List<NpcNeed>.has(need: String) : Boolean {
    return this.firstOrNull()?.key == need
}
