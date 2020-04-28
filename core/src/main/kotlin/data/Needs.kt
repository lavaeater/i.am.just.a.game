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
            return costs[needsToActivities[need]]?: (costs[Activity.Neutral] ?: error("Actitivy Neutral has no cost, this is wrong"))
        }

        fun getCostForActivity(activity: String) : Cost {
            return costs[activity] ?: costs[Activity.Neutral] ?: error("Activity Neutral has no cost, which is wrong")
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
                Activity.Working to Cost(
                        Activity.Working, mapOf(
                        Money to -30,
                        Fuel to 16,
                        Rest to 8,
                        Social to 2
                )),
                Activity.Sleeping to Cost(
                        Activity.Sleeping, mapOf(
                        Money to 0,
                        Fuel to 0,
                        Rest to -12,
                        Social to 0
                )),
                Activity.Socializing to Cost(
                        Activity.Socializing, mapOf(
                        Money to 40,
                        Fuel to 20,
                        Rest to 2,
                        Social to -48
                )),
                Activity.Eating to Cost(Activity.Eating, mapOf(
                        Money to 24,
                        Fuel to -64,
                        Rest to 0,
                        Social to 4
                )),
                Activity.Neutral to Cost(Activity.Neutral),
                Activity.OnTheMove to Cost(Activity.OnTheMove, mapOf(
                        Money to 0,
                        Fuel to 2,
                        Rest to 0,
                        Social to 2)))

        val needsToActivities = mapOf(
                Fuel to Activity.Eating,
                Rest to Activity.Sleeping,
                Money to Activity.Working,
                Social to Activity.Socializing
        )

        val prioritizedNeeds = listOf(
                Fuel,
                Rest,
                Money,
                Social,
                Nothing
        )

        val statesToNeeds = mapOf(
                Activity.Working to Money,
                Activity.Sleeping to Rest,
                Activity.Eating to Fuel,
                Activity.Socializing to Social
        )

        val activitiesToEvents = mapOf(
                Activity.Working to Events.StartedWorking,
                Activity.Eating to Events.StartedEating,
                Activity.Sleeping to Events.FellAsleep,
                Activity.Socializing to Events.StartedSocializing
        )

        val movingStates = setOf(
                Activity.OnTheMove,
                Activity.GoingToEat,
                Activity.GoingToWork,
                Activity.GoingHomeToSleep,
                Activity.GoingToMeetAFriend
        )

        val lowRange = -24..24
        val normalRange = 25..72
        val lowNormal = lowRange.first..normalRange.last
        val greatRange = 73..128 //Max is more like close to 96, but wealth can take us higher, and drugs and so on
        val goodRange = normalRange.first..greatRange.last
        val fullRange = lowRange.first..goodRange.last
    }
}

