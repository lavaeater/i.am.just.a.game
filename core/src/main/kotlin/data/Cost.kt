package data

data class Cost(val activity: String, val costMap: Map<String, Int> = mapOf(
        Needs.Fuel to 4,
        Needs.Rest to 4,
        Needs.Money to 4
))