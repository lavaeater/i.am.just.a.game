package data

class NpcStats(val stats: Stats = defaultStats.copy()) {
    private val statsList get() = stats.stats

    val topNeed get() = statsList.sortedWith(compareBy(Stat::value, Stat::priority, Stat::essential)).first()
            /*
        The top need can be gotten by ordering by... value and priority?
        We need some way of sorting stats. This is why we need a class for them

        Priorities could be different for every person, of course. Perhaps
        "essentiality" is in effect something that also determines priority?
         */

    fun applyCosts(costs: Map<String, Int>) {
        for((key, value) in costs)
            applyCost(key, value)
    }

    fun applyCost(key: String, cost: Int) {
        val stat = statsList.firstOrNull { it.key == key }
        if(stat != null) {
            stat.value = (stat.value - cost).coerceIn(stat.validRange) //To accomodate wealth getting real high
        }
    }

    fun applyReplenishments(replenishments: Map<String, Int>) {
        for((key, value) in replenishments)
            applyReplenishment(key, value)
    }

    fun applyReplenishment(key: String, value: Int) {
        applyCost(key, -value)
    }

    companion object {
        val defaultStats = Stats(setOf(
                Stat(Needs.Fuel, 72, 0, true),
                Stat(Needs.Rest, 72, 1, true),
                Stat(Needs.Money, 72, 2, false, Int.MIN_VALUE..Int.MAX_VALUE),
                Stat(Needs.Social, 72, 3, false),
                Stat(Needs.Fun, 72, 4, false)
        ))
    }
}

data class Stats(val stats: Set<Stat>)

data class Stat(val key: String, var value: Int, val priority: Int, val essential: Boolean = false, val validRange: IntRange = 0..100)