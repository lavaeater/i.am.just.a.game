package ai.npc

import data.Needs
import data.Npc
import screens.Mgo

class Satisfiers {

    companion object {
        val satisfiableResolvers = mapOf(
                Needs.Fuel to { npc: Npc -> canIEatHere(npc) },
                Needs.Money to { npc: Npc -> isThisWhereIWork(npc) },
                Needs.Social to { npc: Npc -> doIHaveAFriendNearby(npc) },
                Needs.Rest to { npc: Npc -> canISleepHere(npc) })

        private fun doIHaveAFriendNearby(npc: Npc) : Boolean {

            val doI = npc.friends.any { npc.circleOfConcern.contains(it.currentPosition) }
            return doI
         }

        val whereToSatisfyResolvers = mapOf(
                Needs.Rest to { npc: Npc -> listOf(npc.home) },
                Needs.Money to  { npc: Npc -> listOf(npc.workPlace) },
                Needs.Fuel to { npc: Npc -> Mgo.restaurants}
        )

        fun isThisWhereIWork(npc: Npc) :Boolean {
            return npc.workPlace.box.contains(npc.currentPosition)
        }

        fun canIEatHere(npc: Npc) : Boolean {
            return Mgo.restaurants.any { it.box.contains(npc.currentPosition) }
        }

        fun canISleepHere(npc: Npc) : Boolean {
            return npc.home.box.contains(npc.currentPosition)
        }
    }
}

