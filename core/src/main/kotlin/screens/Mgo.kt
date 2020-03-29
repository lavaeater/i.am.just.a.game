package screens

import com.badlogic.gdx.math.Rectangle
import data.Npc
import ktx.math.amid
import ktx.math.random
import ktx.math.vec2

class Mgo {
    companion object {
        val npcs = mutableListOf<Npc>()
        val workPlaceRange = 100f amid 200f
        val homeXRange = -100f amid 200f
        val homeYRange = -100f amid 200f
        val numberOfNpcs = 2000
        val numberOfWorkPlaces = numberOfNpcs / 5
        val numberOfRestaurants = numberOfNpcs / 2

        val sizeRange = 5f..10f

        val workPlaces = (1..numberOfWorkPlaces).map {
            Place(box = Rectangle(workPlaceRange.random(), workPlaceRange.random(), sizeRange.random(), sizeRange.random()))
        }

        val restaurants = (1..numberOfRestaurants).map {
            Place(type = PlaceType.Restaurant, box = Rectangle(workPlaceRange.random(), workPlaceRange.random(), sizeRange.random(), sizeRange.random()))
        }

        val homeAreas = mutableListOf<Place>()

        val allPlaces get() = workPlaces + restaurants + homeAreas
    }
}