import data.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object NpcStatTest : Spek({
    describe("Top Need should Be correct") {
        it("needs are sorted properly") {
            val npcStat = NpcStats(Stats(setOf(
                    Stat("TopPriority", 100, 0, false),
                    Stat("LowPriority", 10, 1, false)
            )))
            assert(npcStat.topNeed.key == "LowPriority") { "Top Need Key should be LowPriority" }
        }
        it("needs are sorted properly") {
            val npcStat = NpcStats(Stats(setOf(
                    Stat("TopPriority", 100, 0, false),
                    Stat("LowPriority", 100, 1, false)
            )))
            assert(npcStat.topNeed.key == "TopPriority") { "Top Need Key should be TopPriority" }
        }
        it("needs are sorted properly") {
            val npcStat = NpcStats(Stats(setOf(
                    Stat("TopPriority", 100, 1, false),
                    Stat("LowPriority", 100, 1, true)
            )))
            assert(npcStat.topNeed.key == "LowPriority") { "Top Need Key should be LowPriority" }
        }
//        it("needs are sorted properly") {
//            val npcStat = NpcStats(Stats(setOf(
//                    Stat("TopPriority", 100, 0, false),
//                    Stat("LowPriority", 10, 1, false)
//            )))
//            assert(npcStat.topNeed.key == "LowPriority") { "Top Need Key should be LowPriority" }
//        }
    }
})