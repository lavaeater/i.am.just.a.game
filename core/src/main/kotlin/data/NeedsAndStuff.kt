package data

class NeedsAndStuff {
    companion object {
        val costs = mapOf(
                Activity.Working to Cost(
                        Activity.Working, mapOf(
                        Needs.Money to -16,
                        Needs.Fuel to 8,
                        Needs.Rest to 6
                )),
                Activity.Sleeping to Cost(
                        Activity.Sleeping, mapOf(
                        Needs.Money to 0,
                        Needs.Fuel to 0,
                        Needs.Rest to -16
                )),
                Activity.Eating to Cost(Activity.Eating, mapOf(
                        Needs.Money to -8,
                        Needs.Fuel to -16,
                        Needs.Rest to 0
                )),
                Activity.Neutral to Cost(Activity.Neutral),
                Activity.OnTheMove to Cost(
                        Activity.OnTheMove, mapOf(
                        Needs.Money to 0,
                        Needs.Fuel to 8,
                        Needs.Rest to 8)))

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

        val activitiesToEvents = mapOf(
                Activity.Working to Events.StartedWorking,
                Activity.Eating to Events.StartedEating,
                Activity.Sleeping to Events.FellAsleep
        )

        val movingStates = setOf(
                Activity.OnTheMove,
                Activity.GoingToEat,
                Activity.GoingToWork,
                Activity.GoingHomeToSleep
        )

        val lowRange = -24..24
        val normalRange = 25..72
        val greatRange = 73..128 //Max is more like close to 96, but wealth can take us higher, and drugs and so on
        val goodRange = normalRange.first..greatRange.last
        val fullRange = lowRange.first..goodRange.last
    }
}