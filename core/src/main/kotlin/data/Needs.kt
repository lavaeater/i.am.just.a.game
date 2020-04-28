package data

class Needs {
    companion object {
        const val Fuel = "Fuel"
        const val Rest = "Rest"
        const val Money = "Money"
        const val Social = "Social"
        const val Nothing = "Nothing"
        const val Fun = "Fun"

        fun getCostForNeed(need:String) : Cost {
            return costs[needsToActivities[need]]?: (costs[Activities.Neutral] ?: error("Actitivy Neutral has no cost, this is wrong"))
        }

        fun getCostForActivity(activity: String) : Cost {
            return costs[activity] ?: costs[Activities.Neutral] ?: error("Activity Neutral has no cost, which is wrong")
        }

        /**
         * Costs are PER hour
         *
         * Timing should be so that sleep is required every 16 hours.
         *
         * We tick 15 minutes per tick, this means that Rest should deplete
         * at a rate of... 96 - (16 * 60r)
         *
         * and costs should then be per hour! Sleep should deteriorate at
         * rate 6, circa
         *
         * This means that to eat every 5 hours, food needs to deteriorate faster:
         *
         * around 20 per hour.
         *
         * To want to meet someone every day, we need social to deteriorate at 96 / 24 = 4
         *
         * Reverse, sleep up to full in approx 6 hours...12
         *
         * This is very good.
         */
        private val costs = mapOf(
                Activities.Working to Cost(
                        Activities.Working, mapOf(
                        Money to -30,
                        Fuel to 16,
                        Rest to 8,
                        Social to 2
                )),
                Activities.Sleeping to Cost(
                        Activities.Sleeping, mapOf(
                        Rest to -12
                )),
                Activities.Socializing to Cost(
                        Activities.Socializing, mapOf(
                        Money to 40,
                        Fuel to 20,
                        Rest to 2,
                        Social to -48
                )),
                Activities.Eating to Cost(Activities.Eating, mapOf(
                        Money to 24,
                        Fuel to -64,
                        Social to 4
                )),
                Activities.Neutral to Cost(Activities.Neutral),
                Activities.OnTheMove to Cost(Activities.OnTheMove, mapOf(
                        Fuel to 2,
                        Social to 2)))

        val needsToActivities = mapOf(
                Fuel to Activities.Eating,
                Rest to Activities.Sleeping,
                Money to Activities.Working,
                Social to Activities.Socializing
        )

        val prioritizedNeeds = listOf(
                Fuel,
                Rest,
                Money,
                Fun,
                Social,
                Nothing
        )

        val statesToNeeds = mapOf(
                Activities.Working to Money,
                Activities.Sleeping to Rest,
                Activities.Eating to Fuel,
                Activities.Socializing to Social
        )

        val activitiesToEvents = mapOf(
                Activities.Working to Events.StartedWorking,
                Activities.Eating to Events.StartedEating,
                Activities.Sleeping to Events.FellAsleep,
                Activities.Socializing to Events.StartedSocializing
        )

        val movingStates = setOf(
                Activities.OnTheMove,
                Activities.GoingToEat,
                Activities.GoingToWork,
                Activities.GoingHomeToSleep,
                Activities.GoingToMeetAFriend
        )

        val lowRange = -24..24
        val normalRange = 25..72
        val lowNormal = lowRange.first..normalRange.last
        val greatRange = 73..128 //Max is more like close to 96, but wealth can take us higher, and drugs and so on
        val goodRange = normalRange.first..greatRange.last
        val fullRange = lowRange.first..goodRange.last
    }
}

