package data

sealed class PlaceType() {
    object Workplace : PlaceType()
    object Restaurant : PlaceType()
    object Home : PlaceType()
    object Tivoli : PlaceType()
    object TravelHub : PlaceType()
}