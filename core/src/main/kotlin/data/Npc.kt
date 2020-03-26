package data

import ktx.log.info
import ktx.math.amid
import ktx.math.random
import ktx.math.vec2
import statemachine.StateMachine
import systems.TimeSystem
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


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

    private val npcStateMachine = StateMachine.buildStateMachine<States, Events>(States.Neutral, ::myStateHasChanged,{
        state(States.Neutral) {
            edge(Events.GotHungry, States.Hungry) {}
            edge(Events.GotTired, States.Tired) {}
            edge(Events.GotBored, States.Bored) {}
            edge(Events.GotPoor, States.Poor) {}
            edge(Events.GotLonely, States.Lonely) {}
            edge(Events.GoSomewhere, States.MovingAbout) {}
        }
        state(States.MovingAbout) {
            edge(Events.GotHungry, States.Hungry) {}
            edge(Events.GotTired, States.Tired) {}
            edge(Events.GotBored, States.Bored) {}
            edge(Events.GotPoor, States.Poor) {}
            edge(Events.GotLonely, States.Lonely) {}
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
        state(States.Bored) {
            edge(Events.StartHavingFun, States.HavingFun) {}
        }
        state(States.HavingFun) {
            edge(Events.HadSomeFun, States.Neutral) {}
        }
        state(States.Poor) {
            edge(Events.Work, States.Working) {}
        }
        state(States.Working) {
            edge(Events.MadeSomeMoney, States.Neutral) {}
        }
        state(States.Lonely) {
            edge(Events.StartSocializing, States.Socializing) {}
        }
        state(States.Socializing) {
            edge(Events.MetSomePeople, States.Neutral) {}
        }
    })


    private fun myStateHasChanged(newState: States) {
        /*
        React to new state, perhaps? Set a property!
         */
        info { "$name is now $newState, used to be $npcState" }
        npcState = newState
    }

    var thisIsWhereIWantToBe = vec2(0f,0f)

    fun timeHasPassed(minutesPassed: Long = 15) {
        val timeFactor = (60/minutesPassed).toInt()
        //This is only for energy expenditure
        when(npcState) {
            States.HavingFun -> havingFun(timeFactor)
            States.Eating -> eat(timeFactor)
            States.Working -> work(timeFactor)
            States.Sleeping -> sleep(timeFactor)
            States.Socializing -> socialize(timeFactor)
            else -> normalActivity(timeFactor)
        }

        checkNpcState()

        info { "$name is now $npcState" }
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
        if(fuel in lowRange) {
            npcStateMachine.acceptEvent(Events.GotHungry)
            return
        }

        if(rested in lowRange) {
            npcStateMachine.acceptEvent(Events.GotTired)
            return
        }
        if(money in lowRange) {
            npcStateMachine.acceptEvent(Events.GotPoor)
            return
        }
        if(social in lowRange) {
            npcStateMachine.acceptEvent(Events.GotLonely)
            return
        }
        if(boredom in lowRange) {
            npcStateMachine.acceptEvent(Events.GotBored)
            return
        }
    }

    private val lowRange = Int.MIN_VALUE..24
    private val normalRange = 25..72
    private val greatRange = 73..Int.MAX_VALUE //Max is more like close to 96, but wealth can take us higher, and drugs and so on
    private val goodRange = normalRange + greatRange

    private var rested = 72
    private var fuel = 72
    private var money = (64 amid 52).random()
    private var social = 72
    private var boredom = 72


    private fun sleep(timeFactor: Int) {
        rested += 9 /timeFactor + 1
        if(rested in goodRange)
            npcStateMachine.acceptEvent(Events.WakeUp)
    }

    private fun work(timeFactor: Int) {
        rested -= 6 / timeFactor
        fuel -= 12 / timeFactor
        social -= 1
        boredom -= 12 / timeFactor
        money += (6 amid 3).random()
        if(money in goodRange)
            npcStateMachine.acceptEvent(Events.MadeSomeMoney)
    }

    private fun eat(timeFactor: Int) {
        fuel += 18 / timeFactor + 1
        money -= 18 / timeFactor //Hmm.
        if(fuel in goodRange)
            npcStateMachine.acceptEvent(Events.FinishedEating)
    }

    private fun normalActivity(timeFactor: Int) {
        rested -= 4 / timeFactor
        fuel -= 9 / timeFactor + 1
        social -= 2 / timeFactor + 1
        boredom -= 6 / timeFactor
    }

    fun goSomeWhere() {
        if(npcState != States.MovingAbout && npcStateMachine.acceptEvent(Events.GoSomewhere)) {
            val r = 0f amid 100f
            thisIsWhereIWantToBe = vec2(r.random(), r.random())
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

