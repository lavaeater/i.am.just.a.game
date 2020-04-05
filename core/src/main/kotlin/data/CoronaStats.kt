package data

class CoronaStats {
    companion object {
        var currentNpc: Npc? = null
        var infected = 0
        var susceptible = 0
        var dead = 0
        var recovered = 0
        var asymptomatic = 0
        var symptomaticThatStayAtHome = 0
        const val SymptomaticLimit = 5
        const val RecoveryLimit = 13
        const val ChanceOfDeath = 4
        const val ChangeOfRecovery = 15
    }
}