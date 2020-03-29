package screens

sealed class PlaceType {
    object Workplace : PlaceType()
    object Restaurant : PlaceType()
    object Home : PlaceType()
    object Tivoli : PlaceType()
}