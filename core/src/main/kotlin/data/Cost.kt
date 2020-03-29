package data
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
data class Cost(val activity: String, val costMap: Map<String, Int> = mapOf(
        Needs.Fuel to 20,
        Needs.Rest to 8,
        Needs.Money to 0,
        Needs.Social to 4))