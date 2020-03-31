package screens

sealed class PlaceType() {
    object Workplace : PlaceType()
    object Restaurant : PlaceType()
    object Home : PlaceType()
    object Tivoli : PlaceType()
    /*
    Hubs can evolve later to actually have names, enabling fast travel

    Travel is now a form of teleportation.

    We might remove box2d as a refactoring later.
     */
    class TravelHub(val hubType: String = Suburban) : PlaceType()

    companion object {
        val Suburban = "Suburban"
        val Central = "Central"
    }
}