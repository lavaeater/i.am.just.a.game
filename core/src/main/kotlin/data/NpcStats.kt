package data

class NpcStats(val stats: Set<Stat> = setOf(
)) {

    val topNeed
        get() {
            /*
        The top need can be gotten by ordering by... value and priority?
        We need some way of sorting stats. This is why we need a class for them
         */
        }

    companion object {
        val defaultStats = Stats(setOf(
                Stat(Needs.Fuel, 72, 5, false)
        ))
    }
}

data class Stats(val stats: Set<Stat>)

data class Stat(val key: String, var value: Int, val priority: Int, val essential; Boolean = false)