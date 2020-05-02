import data.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object NpcStatTest : Spek({
    describe("TopNeed always contains correct need") {
        it("Lowest value is tops") {
            val npcStat = NpcStats(Stats(setOf(
                    Stat("TopPriority", 100, 0, false),
                    Stat("LowPriority", 10, 1, false)
            )))
            assert(npcStat.topNeed.key == "LowPriority") { "Top Need Key should be LowPriority" }
        }
        it("When same value, top priority comes out on top") {
            val npcStat = NpcStats(Stats(setOf(
                    Stat("TopPriority", 100, 0, false),
                    Stat("LowPriority", 100, 1, false)
            )))
            assert(npcStat.topNeed.key == "TopPriority") { "Top Need Key should be TopPriority" }
        }
        it("Same value, same priority, essential comes out on top") {
            val npcStat = NpcStats(Stats(setOf(
                    Stat("TopPriority", 100, 1, false),
                    Stat("LowPriority", 100, 1, true)
            )))
            assert(npcStat.topNeed.key == "LowPriority") { "Top Need Key should be LowPriority" }
        }
        it("Same value, same priority, essential comes out on top") {
            val npcStat = NpcStats(Stats(setOf(
                    Stat("TopPriority", 100, 1, true),
                    Stat("LowPriority", 100, 1, false)
            )))
            assert(npcStat.topNeed.key == "TopPriority") { "Top Need Key should be LowPriority" }
        }
    }
    describe("Applying costs works") {
        val npcStat  = NpcStats(Stats(setOf(
                Stat("key", 100, 1, true)
        )))
        it("Applying a cost of five lowers value with five") {
            npcStat.applyCost("key", 5)
            assert(npcStat.stats.stats.first { it.key == "key" }.value == 95)
        }
        it("Applying a cost of 105 lowers value to 0") {
            npcStat.applyCost("key", 105)
            assert(npcStat.stats.stats.first { it.key == "key" }.value == 0)
        }
    }
    describe("Applying multiple costs works") {
        val npcStat = NpcStats(Stats(setOf(
                Stat("1", 100, 1, true),
                Stat("2", 100, 1, true)
        )))
        it("Applying a cost of five lowers value with five") {
            npcStat.applyCosts(mapOf("1" to 5, "2" to 5))
            assert(npcStat.stats.stats.all { it.value == 95 })
        }
    }
})