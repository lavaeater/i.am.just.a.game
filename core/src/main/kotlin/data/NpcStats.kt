package data

data class NpcStats(val statsMap: MutableMap<String, Int> = mutableMapOf(
        Needs.Fuel to 72,
        Needs.Money to 72,
        Needs.Rest to 72,
        Needs.Social to 72
))