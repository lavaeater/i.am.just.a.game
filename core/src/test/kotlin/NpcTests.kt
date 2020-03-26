import data.Npc
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object NpcTests : Spek( {
    describe("An NPC") {
        val npc = Npc("Test Bertsson", "id")
        describe("") {
            it("Gets hungry or something") {
//                assert()
            }
        }
    }
})