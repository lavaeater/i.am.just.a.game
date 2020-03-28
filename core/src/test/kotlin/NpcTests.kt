import data.Needs
import data.Npc
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object NpcTests : Spek( {
    describe("An NPC") {
        val npc = Npc("Test Bertsson", "id")
        describe("after time has passed so it has all needs") {
            while (npc.npcNeeds.count() < 5 && !npc.npcNeeds.any { it.key == Needs.Fuel }) {
                npc.timeHasPassed()
            }
            it("needs are sorted properly") {
                assert(npc.hasNeed(Needs.Fuel)) { "Some kind of lazy message" }
            }
        }
    }
})