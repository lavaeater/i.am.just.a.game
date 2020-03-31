package data

class NeedsAndStuff {
    companion object {
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
                        Needs.Money to -15,
                        Needs.Fuel to 16,
                        Needs.Rest to 8,
                        Needs.Social to 2
                )),
                Activity.Sleeping to Cost(
                        Activity.Sleeping, mapOf(
                        Needs.Money to 0,
                        Needs.Fuel to 0,
                        Needs.Rest to -12,
                        Needs.Social to 0
                )),
                Activity.Socializing to Cost(
                        Activity.Socializing, mapOf(
                        Needs.Money to 40,
                        Needs.Fuel to 20,
                        Needs.Rest to 2,
                        Needs.Social to -48
                )),
                Activity.Eating to Cost(Activity.Eating, mapOf(
                        Needs.Money to 48,
                        Needs.Fuel to -64,
                        Needs.Rest to 0,
                        Needs.Social to 4
                )),
                Activity.Neutral to Cost(Activity.Neutral),
                Activity.OnTheMove to Cost(Activity.OnTheMove, mapOf(
                        Needs.Money to 0,
                        Needs.Fuel to 2,
                        Needs.Rest to 0,
                        Needs.Social to 2)))

        val needsToActivities = mapOf(
                Needs.Fuel to Activity.Eating,
                Needs.Rest to Activity.Sleeping,
                Needs.Money to Activity.Working,
                Needs.Social to Activity.Socializing
        )

        val statesToNeeds = mapOf(
                Activity.Working to Needs.Money,
                Activity.Sleeping to Needs.Rest,
                Activity.Eating to Needs.Fuel,
                Activity.Socializing to Needs.Social
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
        val greatRange = 73..128 //Max is more like close to 96, but wealth can take us higher, and drugs and so on
        val goodRange = normalRange.first..greatRange.last
        val fullRange = lowRange.first..goodRange.last
    }
}