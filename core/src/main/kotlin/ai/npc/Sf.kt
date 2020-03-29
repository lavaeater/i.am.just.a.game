package ai.npc

import data.Needs
import data.Npc
import screens.Mgo

class Sf {

    companion object Sf {
        val satisfiableResolvers = mapOf(
                Needs.Fuel to { npc: Npc -> canIEatHere(npc) },
                Needs.Money to { npc: Npc -> isThisWhereIWork(npc) },
                Needs.Rest to { npc: Npc -> canISleepHere(npc) })

        val whereToSatisfyResolvers = mapOf(
                Needs.Rest to { npc: Npc -> npc.home },
                Needs.Money to  { npc: Npc -> npc.workPlace },
        Needs.Fuel to { npc: Npc -> Mgo.restaurants.random()}
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

