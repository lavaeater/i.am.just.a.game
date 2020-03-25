package data

/*
How to handle NPC state?

Every human has NEEDS

food
sex
money
power

But not all humans have the same level of those
NEEDS

Need        Priority    Level
affection   3          10
fuel        1         5
rest        2         6
power       3          8
wealth      No          7

The needs and their level of fulfillment decide the
actions of a human.

If we need affection but get none, we will have lower fulfillment of that need

Base needs will trigger DESPERATION in humans

So, a humans state could be

Socializing


fatigue
social status
sociability

Reduce

Plan first!

 */

enum class NeedsTier {
    Survival,
    Social,
    Ego,
    Actualization
}


data class Need(val key:String, val tier: NeedsTier)

object NeedsAndStuff {
    const val fuel = "fuel"
    const val love = "love"
    const val friends = "friends"
    const val wealth = "wealth"
    const val rest = "rest"
    const val power = "power"
    const val safety = "safety"
    const val knowledge = "knowledge"

    val needs = listOf(
            Need(fuel, NeedsTier.Survival),
            Need(love, NeedsTier.Social),
            Need(friends, NeedsTier.Social),
            Need(wealth,NeedsTier.Ego),
            Need(rest, NeedsTier.Survival),
            Need(power,NeedsTier.Ego),
            Need(knowledge,NeedsTier.Actualization),
            Need(safety, NeedsTier.Survival)
    )
}

enum class NpcStateOfMind {
    Content,
    Anxious,
    Tired,
    Hungry,
    Aggressive,
    Afraid,
    Violent,
    Curious,
    Friendly
}