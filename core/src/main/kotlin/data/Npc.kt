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
    MovingAbout,
    Hungry,
    Tired,
    Bored,
    Poor,
    Eating,
    Sleeping,
    Lonely,
    Working,
    HavingFun,
    Socializing
}

enum class Events {
    StartEating,
    FinishedEating,
    StartSleeping,
    WakeUp,
    GotLonely,
    GotBored,
    GotHungry,
    GotTired,
    GotPoor,
    HadSomeFun,
    MadeSomeMoney,
    Work,
    MetSomePeople,
    GoSomewhere,
    GotSomewhere,
    StartHavingFun,
    StartSocializing
}

class Npc(val name: String, val id: String) {
    var npcState : States = States.Neutral
        private set

    val npcStats = NpcStats()

    private val npcStateMachine = StateMachine.buildStateMachine<States, Events>(States.Neutral, ::myStateHasChanged,{
        state(States.Neutral) {
            edge(Events.GotHungry, States.Hungry) {}
            edge(Events.GotTired, States.Tired) {}
            edge(Events.GoSomewhere, States.MovingAbout) {}
        }
        state(States.MovingAbout) {
            edge(Events.GotHungry, States.Hungry) {}
            edge(Events.GotTired, States.Tired) {}
            edge(Events.GotSomewhere, States.Neutral) {}
        }
        state(States.Hungry) {
            edge(Events.StartEating, States.Eating) {}
        }
        state(States.Eating) {
            edge(Events.FinishedEating, States.Neutral) {}
        }
        state(States.Tired) {
            edge(Events.StartSleeping, States.Sleeping) {}
        }
        state(States.Sleeping) {
            edge(Events.WakeUp, States.Neutral) {}
        }
    })

    init {
        npcStateMachine.initialize() //Required to make states possible
    }


    private fun myStateHasChanged(newState: States) {
        /*
        React to new state, perhaps? Set a property!
         */
        info { "$name got $newState after $npcState, $npcStats" }
        npcState = newState
    }

    var thisIsWhereIWantToBe = vec2(0f,0f)

    fun timeHasPassed(minutesPassed: Long = 15) {
        val timeFactor = (60/minutesPassed).toInt()

        applyCosts(timeFactor)
        checkNpcState()
    }

    private fun applyCosts(timeFactor: Int) {
        val cost = NpcActivities.activities.first { it.state == npcState }

        npcStats.fuel -= cost.fuelCost
        npcStats.rested -= cost.restCost
        npcStats.money -= cost.moneyCost
        npcStats.social -= cost.socialCost
        npcStats.boredom -= cost.boredomCost
    }

    private fun socialize(timeFactor: Int) {
        rested -= 6 / timeFactor
        fuel -= 4 / timeFactor + 1
        social += 9 / timeFactor + 1
        boredom += 4 / timeFactor
        if(social in goodRange)
            npcStateMachine.acceptEvent(Events.MetSomePeople)
    }

    private fun havingFun(timeFactor: Int) {
        rested -= 6 / timeFactor
        fuel -= 9 / timeFactor + 1
        social += 2 / timeFactor + 1
        boredom += 12 / timeFactor
        if(boredom in goodRange)
            npcStateMachine.acceptEvent(Events.HadSomeFun)
    }

    private fun checkNpcState() {

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







        if(fuel in lowRange) {
            if(npcState != States.Eating && npcState != States.Hungry) {
                npcStateMachine.acceptEvent(Events.GotHungry)
            }
            return
        }

        if(rested in lowRange) {
            if (npcState != States.Sleeping && npcState != States.Tired) {
                npcStateMachine.acceptEvent(Events.GotTired)
            }
            return
        }
        if(money in lowRange) {
            if (npcState != States.Working && npcState != States.Poor) {
                npcStateMachine.acceptEvent(Events.GotPoor)
            }
            return
        }

        if(social in lowRange) {
            if (npcState != States.Socializing && npcState != States.Lonely) {
                npcStateMachine.acceptEvent(Events.GotLonely)
            }
            return
        }
        if(boredom in lowRange) {
            if (npcState != States.HavingFun && npcState != States.Bored) {
                npcStateMachine.acceptEvent(Events.GotBored)
            }
            return
        }
    }

    private val lowRange = -24..24
    private val normalRange = 25..72
    private val greatRange = 73..128 //Max is more like close to 96, but wealth can take us higher, and drugs and so on
    private val goodRange = normalRange + greatRange


    private fun sleep(timeFactor: Int) {
        if(rested in goodRange)
            npcStateMachine.acceptEvent(Events.WakeUp)
    }

    private fun work(timeFactor: Int) {
        if(money in goodRange)
            npcStateMachine.acceptEvent(Events.MadeSomeMoney)
    }

    private fun eat(timeFactor: Int) {
        if(fuel in goodRange)
            npcStateMachine.acceptEvent(Events.FinishedEating)
    }

    private fun normalActivity(timeFactor: Int) {
    }

    fun goSomeWhere() {
        if(npcState == States.Neutral) {
            val r = 0f amid 20f
            thisIsWhereIWantToBe = vec2(r.random(), r.random())
            npcStateMachine.acceptEvent(Events.GoSomewhere)
        }

    }

    fun gotSomewhere() {
        //Just try to change to neutral...
        npcStateMachine.acceptEvent(Events.GotSomewhere)
    }

    fun startEating() {
        if(npcState == States.Hungry)
           npcStateMachine.acceptEvent(Events.StartEating)
    }

    fun startSleeping() {
        if(npcState == States.Tired)
            npcStateMachine.acceptEvent(Events.StartSleeping)
    }

    fun startWorking() {
        if(npcState == States.Poor)
            npcStateMachine.acceptEvent(Events.Work)
    }

    fun startHavingFun() {
        if(npcState == States.Bored)
            npcStateMachine.acceptEvent(Events.StartHavingFun)
    }

    fun startSocializing() {
        if(npcState == States.Lonely)
            npcStateMachine.acceptEvent(Events.StartSocializing)
    }


}

data class NpcStats(var fuel: Int = 72, var rested: Int = 72, var money: Int = 72, var social: Int = 72, var boredom: Int = 72)
data class NpcActivity(val state: States, val fuelCost: Int = 4, val restCost:Int =4,val moneyCost:Int = 0, val socialCost:Int = 4, val boredomCost:Int = 4)

object NpcActivities {
    val activities = listOf(
            NpcActivity(States.Bored),
            NpcActivity(States.Lonely),
            NpcActivity(States.Poor, socialCost = 6, boredomCost = 6),
            NpcActivity(States.Tired, fuelCost = 1, restCost = 2),
            NpcActivity(States.Hungry, fuelCost = 1, restCost = 6),
            NpcActivity(States.HavingFun, fuelCost = 6, boredomCost = -12),
            NpcActivity(States.Socializing, fuelCost = 6, moneyCost = 2, socialCost = -12, boredomCost = -4, restCost = 2),
            NpcActivity(States.Working, fuelCost = 6, moneyCost = -16, socialCost = -4, boredomCost = 8, restCost = 8),
            NpcActivity(States.Sleeping, fuelCost = 0, socialCost = 1, boredomCost = 0, restCost = 16),
            NpcActivity(States.Eating, fuelCost = -16, socialCost = -4, boredomCost = -2, restCost = -2, moneyCost = 16),
            NpcActivity(States.Neutral),
            NpcActivity(States.MovingAbout, fuelCost = 8, restCost = 8, socialCost = -2, boredomCost = -2)
    )
}