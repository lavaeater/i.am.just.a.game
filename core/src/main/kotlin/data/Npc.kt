package data

import ktx.log.info
import ktx.math.amid
import ktx.math.random
import ktx.math.vec2
import systems.TimeSystem
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

class Npc(val name: String, val id: String) {
    var isDead = false
        private set
    var robberySucceeded = false
        private set
    var robberyStarted = false
        private set
    var robbing: Boolean = false
        private set
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
        return !hangry && !sleepy && !povertyStricken
    }

    val povertyStricken get() = money < 50
    private var isEating = false
    private var isSleeping = false
    lateinit var lastCheck: LocalDateTime
    var tiredness = 50
    var fuel = 50
    var money = 100

    fun hasThereBeenOneHourSinceLastChecking() : Boolean {
        val elapsedHours = Duration.between(lastCheck, TimeSystem.currentDateTime).toHours()
        info { "For $name, $elapsedHours have passed since last checkin"}
        return Duration.between(lastCheck, TimeSystem.currentDateTime).toHours() >= 1
    }

    private fun startSleeping() {
        stopGoingAbout()
        isSleeping=true
    }

    fun sleep(): Boolean {
        if(!isSleeping && sleepy) {
            startSleeping()
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

    private fun stopGoingAbout() {
        onMyWay = false
        reachedDestination = false
    }

    private fun startEating() {
        stopGoingAbout()
        checkin()
        isEating = true
    }

    private fun stopEating() {
        money -= 100
        fuel += 100
        isEating = false
    }

    fun eat(): Boolean {
        if(!isEating) {
            startEating()
        }
        if(isEating && hasThereBeenOneHourSinceLastChecking()) {
            stopEating()
        }
        return isEating
    }

    private fun checkin() {
        lastCheck = TimeSystem.currentDateTime
        info{"$name checked in at ${lastCheck.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))}"}
    }

    fun moveSomeWhere(): Boolean {
        if(!onMyWay) {
            //select target coordinate
            val r = (0f amid 20f )

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
        if(isEating || isSleeping) {
            info { "$name is right now eating or sleeping and therefore not spending energy" }
            return
        }
        tiredness += 10
        fuel -= 10
        info { "$name has $fuel energy left, but is $tiredness tired. But there's $money kr in the pocket" }
    }

    fun iAmThereNow() {
        reachedDestination = true
    }

    fun tryToRobSomeone() {
        if(!robbing && !robberyStarted) {
            stopGoingAbout()
            checkin()
            robbing = true
            robberyStarted = true
        }
    }

    fun robberyFailed() {
        robbing = false
        robberySucceeded = false
    }

    fun robberyWin() {
        robbing = false
        robberySucceeded = true
    }

    fun resetRobbery() {
        robbing = false
        robberySucceeded = false
        robberyStarted = false
    }

    fun commitSuicide() {
        info { "$name killed himself" }
        fuel = 0
        isDead = true
    }
}

