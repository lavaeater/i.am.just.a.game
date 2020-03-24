package data

import ktx.log.info
import ktx.math.amid
import ktx.math.random
import ktx.math.vec2
import systems.TimeSystem
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Npc(val name: String, val id: String) {
    var onMyWay: Boolean = false
        private set

    var thisIsWhereIWantToBe = vec2(0f,0f)
    var reachedDestination: Boolean = false
        private set

    val hangry get() =  fuel < 20
    val sleepy get() = tiredness > 80
    val rested get() = tiredness < 30
    val content get() = isContent()

    private fun isContent(): Boolean {
        return !hangry || !sleepy || !povertyStricken
    }

    val povertyStricken get() = money < 50
    private var isEating = false
    private var isSleeping = false
    lateinit var lastCheck: LocalDateTime
    var tiredness = 0
    var fuel = 100
    var money = 500


    fun scavenge(): Boolean {
        return true
    }

    fun lostInterest() {
        TODO("Not yet implemented")
    }

    fun wander(): Boolean {
        return true
    }

    fun walkTo(): Boolean {
        return true
    }

    fun hasThereBeenOneHourSinceLastChecking() : Boolean {
        return Duration.between(TimeSystem.currentDateTime, lastCheck).get(ChronoUnit.HOURS) > 1
    }

    fun sleep(): Boolean {
        if(!isSleeping && sleepy) {
            //Add check to see if we can sleep where we are - otherwise we need to get home!
            isSleeping = true
        }

        if(isSleeping && hasThereBeenOneHourSinceLastChecking()) {
            tiredness -= 10
        }

        if(isSleeping && rested && (1..2).random() == 2)
            isSleeping = false

        if(isSleeping && tiredness <= 0) {
            isSleeping = false
        }
        return isSleeping
    }

    fun eat(): Boolean {
        if(!isEating) {
            checkin()
            isEating = true
        }
        if(isEating && hasThereBeenOneHourSinceLastChecking()) {
            money -= 100
            fuel += 100
            isEating = false
        }
        return isEating
    }

    private fun checkin() {
        lastCheck = TimeSystem.currentDateTime
    }

    fun moveSomeWhere(): Boolean {
        if(!onMyWay) {
            //select target coordinate
            val r = (0f amid 5f )

            val x = r.random()
            val y = r.random()

            thisIsWhereIWantToBe = vec2(x,y)
            onMyWay = true
        }
        if(reachedDestination) {
            onMyWay = false
            reachedDestination = false
        }
        return onMyWay
    }

    fun expendEnergyAndStuff() {
        tiredness += 10
        fuel -= 10
        info { "$name has $fuel energy left, but is $tiredness tired. But there's $money kr in the pocket" }
    }

    fun iAmThereNow() {
        reachedDestination = true
    }
}

